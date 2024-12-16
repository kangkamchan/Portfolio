## 주요 소스코드

## 환경 설정
- **JDK 버전** : 17 이상
- **MySQL 버전** : 9.0.1
- **MariaDB 버전** : 10.11.1

## 주요 의존성 및 버전
**Spring**
- **Spring Boot** : 3.3.6
- **QueryDSL** : 5.1.0
- **commons-io** : 2.11.0
- **JUnit** : 5.10.2
- **MyBatis** : 3.0.3

---

## JwtToken 검사 애너테이션 구현

### 1. CheckJwtToken 애너테이션 정의
```java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckJwtToken {
    String redirectUri() default "/";
}
```
#### 주요기능
 - 특정 메서드에서 JWT 토큰을 검사하기 위해 사용
 - redirectUri 속성을 통해 인증 실패 시 리다이렉트할 경로 지정

### 2. CheckJwtAspect 클래스 구현

#### 주요기능
 - CheckJwtToken 매너테이션이 선언된 메서드에 대한 AOP 기능 제공
 - extractAccessToken를 통해 HttpRequest의 쿠키에서 JwtToken을 추출하고 이를 통해 인증 상태를 확인함
 - 인증 실패시 redirectWithError 메서드를 통해 에러메시지와 함께 지정한 URI로 리다이렉트함
```java
@Component
@Aspect
@Slf4j
@RequiredArgsConstructor
public class CheckJwtAspect {
    private final CookieUtil cookieUtil;
    @Around("@annotation(checkJwtToken)")
    public Object checkJwtToken(ProceedingJoinPoint joinPoint, CheckJwtToken checkJwtToken) throws Throwable {
        log.debug("Checking jwt token");
        Object[] args = joinPoint.getArgs();
        try{
            String accessToken = extractAccessToken(args);
            if(accessToken==null){
                log.debug("Access token is null");
                return redirectWithError("로그인 상태가 아닙니다.", checkJwtToken.redirectUri(),args);
            }
            log.debug("Access token is not null");
            return joinPoint.proceed();
        }catch(RuntimeException e){
            log.error("RuntimeException 발생", e);
            return redirectWithError("로그인 상태가 아닙니다.", checkJwtToken.redirectUri(),args);
        }catch(Exception e){
            log.error("Exception 발생", e);
            return redirectWithError(e.getMessage(), checkJwtToken.redirectUri(),args);
        }
    }
```
#### extractAccessToken 메서드
 - 메서드의 인자들을 매개변수로 입력받음
 - 메서드 인자 중 HttpServletRequest 객체를 찾아 쿠키에서 JwtToken을 추출함
 - 존재할경우 JwtToken을 반환하고 존재하지 않을경우 null을 반환함

```java
    private String extractAccessToken(Object[] args) throws UnsupportedEncodingException {
        for(Object arg : args){
            if(arg instanceof HttpServletRequest request){
                log.debug("Extracting access token from request");
                request.setCharacterEncoding(StandardCharsets.UTF_8.name());
                String accessToken = cookieUtil.getCookieValue(request,"accessToken");
                if(accessToken!=null && !accessToken.isEmpty()){
                    return accessToken;
                }
            }
        }
        log.debug("Access token is null");
        return null;
    }
```
#### redirectWithError 메서드
 - 인증 실패 시 에러메시지와 함께 리다이렉트 URI를 반환하는 에러처리 메서드
 - 메서드 인자 중 RedirectAttributes 또는 HttpServletRequest를 찾아 메시지를 전달함
 - 해당하는 인자가 없을 경우 쿼리스트링으로 전달함
 ```java
    private Object redirectWithError(String message, String redirectUrl, Object[] args) {
        log.debug("redirectWithError");
        RedirectAttributes redirectAttributes = null;
        HttpServletRequest request = null;
        for (Object arg : args) {
            if (arg instanceof RedirectAttributes ra) {
                log.debug("redirectAttribute");
                redirectAttributes = ra;
            } else if (arg instanceof HttpServletRequest req) {
                log.debug("HttpRequest");
                request = req;
            }
        }
        if (redirectAttributes != null) {
            log.debug("return redirectAttribute");
            redirectAttributes.addFlashAttribute("error", message);
            return "redirect:" + redirectUrl;
        } else if (request != null) {
            log.debug("return request");
            log.debug("return flashMap");
            var flashMap = org.springframework.web.servlet.support.RequestContextUtils.getOutputFlashMap(request);
            flashMap.put("error", message);
            return "redirect:" + redirectUrl;
        }
        String encodedMessage = URLEncoder.encode(message, StandardCharsets.UTF_8);
        return "redirect:" + redirectUrl + "?errors=" + encodedMessage;
    }
}
```

## RESTful API 정의, RestController 및 Ajax통신구현

**[API 정의서](project/swc/REST API.pdf)**

