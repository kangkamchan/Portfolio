package net.fullstack7.mooc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RootController {
    @GetMapping("/")
    public String rootGet() {
        return "redirect:/main/main";
    }
}
