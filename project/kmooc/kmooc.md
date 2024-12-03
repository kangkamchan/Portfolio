# KMOOC 주요 소스코드
---
## 환경 설정
- **JDK 버전** : 
- **Tomcat 버전** : 

## 주요 의존성 및 버전
- **SpringBoot**: 
- **JUnit**: 
- **Servlet API**: 
- **JSTL**: 
- **Log4j**: 
- **Lombok**: 
- **HikariCP**: 
- **MyBatis**: 
- **JPA**:
- **Hibernate**:
---
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
  
