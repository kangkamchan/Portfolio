package net.questionbank.controller;
import jakarta.servlet.ServletException;
import lombok.extern.log4j.Log4j2;
import net.questionbank.annotation.Logging;
import net.questionbank.exception.CustomRuntimeException;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Logging
@Log4j2
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(
            {
                    ServletException.class,
                    HttpRequestMethodNotSupportedException.class,
            }
    )
    public String handleMethodNotAllowedException(ServletException e, RedirectAttributes redirectAttributes) {
        log.info("MethodNotAllowedException : {}",e.getMessage());
        redirectAttributes.addFlashAttribute("errors", "잘못된 접근입니다.");
        return "redirect:/error/error";
    }
    @ExceptionHandler(CustomRuntimeException.class)
    public String handleCustomRuntimeExceptionException(CustomRuntimeException e, RedirectAttributes redirectAttributes) {
        log.info("customRuntimeException : {}",e.getMessage());
        redirectAttributes.addFlashAttribute("errors", e.getMessage());
        return "redirect:/error/error";
    }
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public String handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e, RedirectAttributes redirectAttributes) {
        log.info("MethodArgumentTypeMismatchException : {}",e.getMessage());
        redirectAttributes.addFlashAttribute("errors", e.getMessage());
        return "redirect:/error/error";
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgumentExceptionException(IllegalArgumentException e, RedirectAttributes redirectAttributes) {
        log.info("IllegalArgumentException : {}",e.getMessage());
        redirectAttributes.addFlashAttribute("errors", e.getMessage());
        return "redirect:/error/error";
    }
    @ExceptionHandler(Exception.class)
    public String handleException(Exception e, RedirectAttributes redirectAttributes) {
        log.info("Exception : {}",e.getMessage());
        redirectAttributes.addFlashAttribute("errors", "일시적인 오류가 발생했습니다. 다시 시도하세요.");
        return "redirect:/error/error";
    }
}
