package net.fullstack7.mooc.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.fullstack7.mooc.domain.Subject;
import net.fullstack7.mooc.service.SubjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api/main")
public class CommonApiController {
    private final SubjectService subjectService;

    @GetMapping("/subjects")
    public ResponseEntity<?> headerSubject() {
        try {
            List<Subject> subject = subjectService.getAllSubjects();
            return ResponseEntity.ok(subject);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
}
