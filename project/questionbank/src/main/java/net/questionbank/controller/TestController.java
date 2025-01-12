package net.questionbank.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.*;
import lombok.extern.log4j.Log4j2;
import net.questionbank.annotation.Logging;
import net.questionbank.annotation.RedirectWithError;
import net.questionbank.dto.main.SubjectRequestDTO;
import net.questionbank.dto.test.LargeDTO;
import net.questionbank.dto.presetExam.LargeChapterDTO;
import net.questionbank.dto.test.RequestBodyDTO;
import net.questionbank.dto.test.TempTestImageDTO;
import net.questionbank.dto.textbook.TextBookApiDTO;
import net.questionbank.exception.CustomRuntimeException;
import net.questionbank.service.step3.Step3Service;
import net.questionbank.service.test.TestServiceIf;
import net.questionbank.service.textbook.TextbookServiceIf;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@Logging
@Log4j2
@RequestMapping("/customExam")
public class TestController {
    private final Step3Service step3Service;
    private final TextbookServiceIf textbookService;
    private final TestServiceIf testService;
    private final SpringTemplateEngine templateEngine;


    @PostMapping("/step0")
    @RedirectWithError(redirectUri = "/error/error")
    public String step0(@RequestParam(required = false) String subjectId, HttpServletRequest req, Model model, RedirectAttributes redirectAttributes, HttpSession session) {
        if (subjectId == null) {
            subjectId = (String) req.getAttribute("subjectId");
        }
        log.info(subjectId);
        TextBookApiDTO textbookDetailDTO = textbookService.getTextbookDetails(subjectId);
        if (textbookDetailDTO == null) {
            throw new CustomRuntimeException("textbook not found");
        }
        session.setAttribute("textbookDetailDTO", textbookDetailDTO);
        model.addAttribute("textbookDetailDTO", textbookDetailDTO);
        List<LargeChapterDTO> largeChapterList = testService.getPresetExamList(subjectId);
        if (largeChapterList == null) {
            throw new CustomRuntimeException("presetExam not found");
        }
        model.addAttribute("largeChapterList", largeChapterList);
        log.info("largeChapterList : {}", largeChapterList);
        return "test/step0";
    }

    @RedirectWithError(redirectUri = "/error/error")
    @PostMapping("/step1")
    public String step1(Model model, HttpSession session) {

        TextBookApiDTO textbookDetailDTO = (TextBookApiDTO) session.getAttribute("textbookDetailDTO");
        if (textbookDetailDTO == null) {
            throw new CustomRuntimeException("textbook not found");
        }
        String subjectId = textbookDetailDTO.getSubjectId().toString();
        SubjectRequestDTO subjectRequestDTO = SubjectRequestDTO.builder()
                .subjectId(subjectId)
                .build();
        List<LargeDTO> largeList = testService.step1(subjectRequestDTO);
        model.addAttribute("largeList", largeList);
        model.addAttribute("subjectId", subjectId);
        model.addAttribute("textbookDetailDTO", textbookDetailDTO);
        return "test/step1";
    }


    /*
    문항 편집(문제 목록, 문제지요약)
    step1에서 받은 과목 id 값
    step2/sub03_01_01로 리턴
     */
    @RedirectWithError(redirectUri = "/error/error")
    @PostMapping("/step2")
    public String getItemIds(Model model,
                             @RequestParam(required = false, name = "examId") String[] examIds,
                             String[] questionIds,
                             @RequestParam(required = false) String strRequestBody,
                             @RequestParam(required = false) String requestLow,
                             @RequestParam(required = false) String requestMiddle,
                             @RequestParam(required = false) String requestHigh) {
        String type = "";
        if (examIds != null) {
            try {
                Map<String, String[]> presetExamMap = testService.getPresetExamQuestions(examIds);
                if (presetExamMap != null) {
                    questionIds = presetExamMap.remove("all");
                    model.addAttribute("presetExamMap", presetExamMap);
                }
                type = "edit";
            } catch (CustomRuntimeException e) {
                log.error("CustomRuntimeException", e);
                throw e;
            } catch (Exception e) {
                log.error("Exception", e);
                throw e;
            }
        }

        //json 파싱
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            RequestBodyDTO requestBodyDTO = objectMapper.readValue(strRequestBody, RequestBodyDTO.class);
            model.addAttribute("requestBodyDTO", requestBodyDTO); //재검색용 requestBody
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        model.addAttribute("questionIds", questionIds);
        model.addAttribute("requestLow", requestLow);
        model.addAttribute("requestMiddle", requestMiddle);
        model.addAttribute("requestHigh", requestHigh);
        model.addAttribute("type", type);
        return "test/step2";
    }

    @PostMapping("/step3")
    public String step3(Model model, @RequestParam(required = false, name = "itemId") List<Long> itemIdList, HttpSession session) {
        TextBookApiDTO textbookDetailDTO = (TextBookApiDTO) session.getAttribute("textbookDetailDTO");

        if (textbookDetailDTO == null) {
            throw new CustomRuntimeException("textbook not found");
        }

        TempTestImageDTO tempTestImageDTO = step3Service.testInfoImage(itemIdList, textbookDetailDTO.getSubjectId());
        tempTestImageDTO.setTextbookApiDTO(textbookDetailDTO);

        model.addAttribute("testInfo", tempTestImageDTO);
        model.addAttribute("subjectId", textbookDetailDTO.getSubjectId());
        model.addAttribute("itemIdList", itemIdList);
        model.addAttribute("pdfFileId", UUID.randomUUID().toString());
        model.addAttribute("descriptive", tempTestImageDTO.getDescriptive().equals("O"));
        return "test/sub04_01";
    }

    @GetMapping("/complete")
    public String complete() {
        return "test/sub04_02";
    }
}

