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