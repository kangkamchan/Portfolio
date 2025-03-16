### TestSeviceImpl
- 문제은행 API에서 시험지 정보를 받아오는 서비스 객체의 주요 메서드인 getPresetExamList 소스코드
```java
@Service
@Transactional
@Logging
@Log4j2
@RequiredArgsConstructor
public class TestServiceImpl implements TestServiceIf {
    private final WebClient webClient;
     /**
     * 과목별 대단원 리스트 대단원별 시험지 리스트 가져오기
     * @param subjectId 과목 코드 문자열
     * @return 대단원 정보, 대단원에 포함된 시험지 리스트 포함한 LargeChapterDTO 객체 리스트
     */
    @Override
    public List<LargeChapterDTO> getPresetExamList(String subjectId) {
        try{
			//WebClient 로 문제은행 API에서 세팅 시험지 정보 요청
            Mono<PresetExamApiResponse> presetExamApiResponseMono = webClient.post()
                    .uri("/chapter/exam-list")
                    .bodyValue(Map.of("subjectId",subjectId))
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, res -> Mono.error(new CustomRuntimeException("시험지정보 조회 중 에러 발생, code : " + res.statusCode())))
                    .bodyToMono(PresetExamApiResponse.class);
            PresetExamApiResponse presetExamApiResponse = presetExamApiResponseMono.block();

            if(presetExamApiResponse == null) {
                throw new CustomRuntimeException("시험지 정보 없음");
            }
			//시험지 각각의 대단원정보, 시험지정보 객체 리스트
            List<PresetExamResponseDTO> presetExamResponseDTOList = presetExamApiResponse.getExamList();

            if(presetExamResponseDTOList == null || presetExamResponseDTOList.isEmpty()) {
                throw new CustomRuntimeException("시험지 정보 없음");
            }
			//시험지를 대단원별로 묶어 대단원 오름차순으로 정렬
			//대단원별 시험지를 List로 묶어놓은 객체
            List<LargeChapterDTO> largeChapterDTOList = new ArrayList<>();
			
			//시험지 정보 각각을 순회하며 LargeChapterDTO 객체로 변환 후 대단원별 중복 없이 List로 묶기 
            presetExamResponseDTOList.forEach(presetExamResponseDTO -> {
				//LargeChapterDTO로 변환
                LargeChapterDTO largeChapterDTO = LargeChapterDTO.builder()
                        .largeChapterId(presetExamResponseDTO.getLargeChapterId())
                        .largeChapterName(presetExamResponseDTO.getLargeChapterName())
                        .presetExams(
                                new ArrayList<>(
                                    List.of(
                                           PresetExamDTO.builder()
                                                .examId(presetExamResponseDTO.getExamId())
                                                .examName(presetExamResponseDTO.getExamName())
                                                .itemCnt(presetExamResponseDTO.getItemCnt())
                                                .build()
                                    )
                                )
                        )
                        .build();
				//largeChapterDTOList 에 대단원이 없으면 그대로 삽입, 있으면 이미 있는 largeChapterDTO의 시험지 리스트에 시험지 정보만 추가
                if(!largeChapterDTOList.contains(largeChapterDTO)) {
                    largeChapterDTOList.add(largeChapterDTO);
                }else{
                    int index = largeChapterDTOList.indexOf(largeChapterDTO);
                    largeChapterDTOList.get(index).getPresetExams().add(
                                PresetExamDTO.builder()
                                        .examId(presetExamResponseDTO.getExamId())
                                        .examName(presetExamResponseDTO.getExamName())
                                        .itemCnt(presetExamResponseDTO.getItemCnt())
                                        .build()
                    );
                }
            });
			//Comparator 활용 대단원 순으로 정렬
            largeChapterDTOList.sort(Comparator.comparingInt(LargeChapterDTO::getLargeChapterId));
            return largeChapterDTOList;
        }catch(Exception e) {
            log.error(e.getMessage());
            throw new CustomRuntimeException("시험지 정보 조회 중 오류 발생");
        }
    }
}
``` 

### Logging
- 적용한 클래스 내부의 메서드 시작과 종료, 경과시간을 로그에 남김
```java
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Logging {}

@Component
@Aspect
@Log4j2
@RequiredArgsConstructor
public class LoggingAspect {
    final String doubleLine = "==============================";

    @Around("@annotation(logging)")
    public Object logMethodLifeCycle(ProceedingJoinPoint joinPoint, Logging logging) throws Throwable {
        String method = joinPoint.getSignature().getName(); //메서드명
        String className = joinPoint.getTarget().getClass().getName(); //클래스명
        Object[] args = joinPoint.getArgs(); //메서드 인수

        //메서드 시작시 로그
        log.info(doubleLine);
        log.info(" {}-> {} start", className, method);
        long startTime = System.currentTimeMillis();

        try{
            Object result = joinPoint.proceed();
            long endTime = System.currentTimeMillis();
            //메서드 종료시 로그
            log.info("{} -> {} end in {} ms", className, method, endTime - startTime);
            return result;
        }catch (Throwable e){
            log.error("error in {}.{}",className,method, e);
            throw e;
        }

    }

    @Around("within(@net.questionbank.annotation.Logging *)")
    public Object logMethodWithin(ProceedingJoinPoint joinPoint) throws Throwable {
        return logMethodLifeCycle(joinPoint, null);
    }
}
```

### RedirectWithError
- 커스텀 오류 메시지와 함께 지정한 URI로 리다이렉트 하는 예외 처리 애너테이션
```java
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RedirectWithError {
    String redirectUri() default "/";
    String attributeName() default "errors";
    String message() default "";
}
```
```java
@Aspect
@Component
@Log4j2
@RequiredArgsConstructor
public class RedirectWithErrorAspect {

    @Around("@annotation(redirectWithError)")
    public Object redirectWithError(ProceedingJoinPoint proceedingJoinPoint, RedirectWithError redirectWithError) throws Throwable {
        final String REDIRECT_URI = redirectWithError.redirectUri();
        final String ATTRIBUTE_NAME = redirectWithError.attributeName();
        final String ERROR_MESSAGE = redirectWithError.message();
        try{
            return proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
        }catch(Throwable e){
            log.error(e.getMessage());
            return redirectWithErrorExecute(ERROR_MESSAGE.isEmpty()?e.getMessage():ERROR_MESSAGE,REDIRECT_URI,ATTRIBUTE_NAME,proceedingJoinPoint.getArgs());
        }
    }
    private Object redirectWithErrorExecute(String message, String redirectUrl, String attributeName, Object[] args) {
        log.debug("redirectWithError");
        RedirectAttributes redirectAttributes = null;
        for (Object arg : args) {
            if (arg instanceof RedirectAttributes ra) {
                log.debug("redirectAttribute");
                redirectAttributes = ra;
            }
        }
        if (redirectAttributes != null) {
            log.debug("return redirectAttribute");
            redirectAttributes.addFlashAttribute(attributeName, message);
        }
        return "redirect:" + redirectUrl;
    }
    @Around("within(@net.questionbank.annotation.RedirectWithError *)")
    public Object within(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        RedirectWithError redirectWithError = proceedingJoinPoint.getTarget()
                .getClass()
                .getAnnotation(RedirectWithError.class);

        if (redirectWithError == null) {
            throw new IllegalStateException("Missing @RedirectWithError annotation");
        }
        return redirectWithError(proceedingJoinPoint, redirectWithError);
    }
}
```