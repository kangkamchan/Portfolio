# KMOOC 주요 소스코드
---
## 환경 설정
- **JDK 버전** : 17
- **MariaDB 버전** : 10.11.9

## 주요 의존성 및 버전
- **SpringBoot**: 3.3.6
- **JUnit**: 5.10.2
- **MyBatis**: 3.0.3
- **QueryDSS**: 5.1.0
---
 
## CourseController.java

### 백엔드 코드
```java
	@GetMapping("/view")
    public String courseView(
			@RequestParam(required = false, defaultValue = "-1") int courseId, 
			HttpSession session, 
			Model model,
			RedirectAttributes redirectAttributes) {
        try{
            if(courseId<=0){
                redirectAttributes.addFlashAttribute("errors","유효하지 않은 값이 입력되었습니다.");
                return "redirect:/course/list/all";
            }
            CourseViewDTO courseViewDTO = courseService.getCourseViewById(courseId);
            if(courseViewDTO == null) {
                redirectAttributes.addFlashAttribute("errors","존재하지 않는 강의입니다.");
                return "redirect:/course/list/all";
            }

            if(!courseViewDTO.getStatus().equals("PUBLISHED")) {
                redirectAttributes.addFlashAttribute("errors","삭제 또는 준비중인 강의입니다.");
                return "redirect:/course/list/all";
            }

            MemberDTO memberDTO = (MemberDTO)session.getAttribute("memberDTO");
            if(memberDTO == null) {
                memberDTO = MemberDTO.builder().memberId("").build();
            }
            CourseEnrollmentDTO courseEnrollmentDTO = courseEnrollmentService.isEnrolled(
                    CourseEnrollmentDTO.builder()
                            .course(Course.builder().courseId(courseId).build())
                            .member(Member.builder().memberId(memberDTO.getMemberId()).build())
                            .build()
            );
            courseViewDTO.setDescription(courseViewDTO.getDescription().replaceAll("[\r\n]+","<br>"));
            log.info("courseViewDTO : {}", courseViewDTO);
            model.addAttribute("courseViewDTO", courseViewDTO);
            model.addAttribute("isEnrolled", courseEnrollmentDTO);
            return "course/view";
        }catch(Exception e){
            log.error(e);
            redirectAttributes.addFlashAttribute("errors", e.getMessage());
            return "redirect:/course/list/all";
        }
    }
```
#### 기능설명
  - CourseController 의 강의 상세보기 부분을 처리하는 메서드
  - courseId 에 대한 유효성 검사 후 상황에 따라 구체적인 에러 메시지를 전달하여 사용자 편의성과 보안성 향상
  - 로그인한 사용자가 해당 강의를 수강중인지 확인 하여 결과에 따라 페이지에서 다른 내용을 전달함
  - 줄바꿈 문자를 HTML의 <br> 태그로 변환하여 사용자 가독성을 향상시킴

#### 개선할 점
  - 중복되는 부분인 오류 메시지를 redirectAttributes에 저장 후 redirect 하는 부분을 메서드로 모듈화
  - PUBLISHED 와 같은 값을 상수로 선언하여 사용
  - 줄바꿈 문자를 HTML로 변환하는 부분에서 HTML 인젝션 방지 방안 필요
---

## 수강신청, 처리 부분

### 백엔드 코드
```java
@RestController
@RequestMapping("/courseEnrollment")
@RequiredArgsConstructor
@Log4j2
public class CourseEnrollmentController {
    private final CourseEnrollmentServiceIf courseEnrollmentService;
    private final CourseService courseService;
    private final ModelMapper modelMapper;
    private final LearningHistoryServiceIf learningHistoryService;
    @PostMapping("/regist/{courseId}")
    public ResponseEntity<?> regist(@PathVariable int courseId, HttpSession session) {
        try{
            MemberDTO memberDTO = (MemberDTO) session.getAttribute("memberDTO");
            if (memberDTO == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message","로그인이 필요합니다."));
            }
            courseEnrollmentService.regist(
                    CourseEnrollmentDTO
                            .builder()
                            .course(Course.builder().courseId(courseId).build())
                            .member(modelMapper.map(memberDTO, Member.class))
                            .enrollmentDate(LocalDateTime.now())
                            .build()
            );
            int learningHistoryResult = learningHistoryService.saveAll(courseId, memberDTO.getMemberId());
            log.info("learning history result: {}",learningHistoryResult);
            return ResponseEntity.ok(ApiResponse.success("수강신청이 완료되었습니다."));
        }catch(Exception e){
            log.error(e);
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    @DeleteMapping("/delete/{courseId}")
    public ResponseEntity<?> delete(@PathVariable int courseId, HttpSession session) {
        try{
            MemberDTO memberDTO = (MemberDTO) session.getAttribute("memberDTO");
            if (memberDTO == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
            }
            CourseEnrollmentDTO courseEnrollmentDTO = courseEnrollmentService.isEnrolled(
              CourseEnrollmentDTO
                      .builder()
                      .course(Course.builder().courseId(courseId).build())
                      .member(Member.builder().memberId(memberDTO.getMemberId()).build())
                      .build()
            );
            if(courseEnrollmentDTO == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error("수강중인 강의가 아닙니다."));
            }
            String result = courseEnrollmentService.delete(courseEnrollmentDTO);
            if(result != null){
                return ResponseEntity.badRequest().body(ApiResponse.error(result));
            }
            learningHistoryService.deleteAll(courseId, memberDTO.getMemberId());
            return ResponseEntity.ok(ApiResponse.success("수강 취소 완료"));
        }catch(Exception e){
            log.error(e);
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}
```
#### 기능설명
  - 수강신청과 수강취소를 처리하는 RestController
  - 로그인정보, 수강여부를 확인하고 인증, 인가 확인 실패시 상황에 맞는 에러메시지를 반환함

#### 개선할 점
  - 자원을 표현하는데 적합하지 않은 URI 네이밍을 적용, 차후 개발시에는 RESTful한 네이밍 규칙과 함께 API정의서를 작성해보도록 하겠음

### 프론트엔드 코드
```javascript
async function courseEnrollment(button) {
	const courseId = button.getAttribute("data-courseId");
	try {
            const response = await fetch(`/courseEnrollment/regist/${courseId}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
            });

	    if (!response.ok) {
                console.log("response not ok");
                const error = await response.json();
                console.log(error.message);
                alert(error.message);
                return;
            }
            console.log("response ok");
            let result = await response.json()
            let flag = confirm(result.message + '\r\n 나의학습방으로 이동하시겠습니까?');
            if(flag){
                location.href="/mypage/myclass";
            }else{
                location.reload();
            }
        } catch (error) {
            console.error('수강신청 에러:', error);
            alert(error.message || '수강신청에 실패했습니다.');
        }
}
```
#### 기능설명
  - 서버에 POST요청을 보내 수강신청을 처리하는 javascript 비동기 함수
  - 성공시 성공 메시지를 출력 후 학습페이지로 이동시킴
  - 실패시 상황에 맞는 에러메시지를 출력함
