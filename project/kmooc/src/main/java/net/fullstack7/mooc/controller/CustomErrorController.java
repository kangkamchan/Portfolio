package net.fullstack7.mooc.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class CustomErrorController {
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public String handleTypeMismatchException(
            MethodArgumentTypeMismatchException ex,
            RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errors", "유효하지 않은 값이 입력되었습니다.");
        return "error/error";
    }
}
