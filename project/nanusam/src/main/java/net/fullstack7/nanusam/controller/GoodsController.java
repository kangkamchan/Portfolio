package net.fullstack7.nanusam.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.fullstack7.nanusam.dto.AlertDTO;
import net.fullstack7.nanusam.dto.FileDTO;
import net.fullstack7.nanusam.dto.GoodsDTO;
import net.fullstack7.nanusam.dto.PageRequestDTO;
import net.fullstack7.nanusam.service.AlertService;
import net.fullstack7.nanusam.service.GoodsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/goods")
public class GoodsController {
    private final GoodsService goodsService;
    private final AlertService alertService;
    //파일주소
//    private final String uploadDir = "/resources/image";

    @GetMapping("/list.do")
    public String list(Model model, @Valid PageRequestDTO pageRequestDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/goods/list.do";
        }
        List<String> status = new ArrayList<>();
        status.add("Y");
        status.add("R");
        pageRequestDTO.setStatus(status);

        model.addAttribute("pageinfo", goodsService.listByPage(pageRequestDTO));
        model.addAttribute("categories", goodsService.codeList("goods"));
        return "goods/list";
    }

    @GetMapping("/regist.do")
    public String registGet(Model model) {
        model.addAttribute("categories", goodsService.codeList("goods"));
        return "goods/regist";
    }

    @PostMapping("/regist.do")
    public String registPost(HttpSession session, @RequestParam(required = false) MultipartFile mainImage
            , @RequestParam(required = false) MultipartFile[] detailImage
            , @Valid GoodsDTO goodsDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            log.info("registPost > bindingResult has errors");
            redirectAttributes.addFlashAttribute("formerrors", bindingResult.getAllErrors());
            redirectAttributes.addFlashAttribute("item", goodsDTO);
            return "redirect:/goods/regist.do";
        }

        if(mainImage.getSize() < 1) {
            redirectAttributes.addFlashAttribute("errors", "대표 사진을 등록해주세요");
            redirectAttributes.addFlashAttribute("item", goodsDTO);
            return "redirect:/goods/regist.do";
        }

        goodsDTO.setMemberId(session.getAttribute("memberId").toString());
        String message = goodsService.regist(goodsDTO);

        if (message != null) {
            redirectAttributes.addFlashAttribute("errors", message);
            redirectAttributes.addFlashAttribute("item", goodsDTO);
            return "redirect:/goods/regist.do";
        }

        String savepath = session.getServletContext().getRealPath("/resources/image");

        try {
            message = upload(mainImage, goodsDTO.getIdx()
                    , "goods_" + goodsDTO.getIdx() + "_0" + getExt(mainImage.getOriginalFilename())
                    , savepath);

            if (message != null) {
                redirectAttributes.addFlashAttribute("item", goodsDTO);
                redirectAttributes.addFlashAttribute("errors", message);
                return "redirect:/goods/regist.do";
            }

            if (detailImage != null && detailImage.length > 0) {
                for (MultipartFile detail : detailImage) {
                    if (detail.getSize() > 0) {
                        message = upload(detail, goodsDTO.getIdx()
                                , "goods_" + goodsDTO.getIdx() + "_z" + UUID.randomUUID().toString() + getExt(detail.getOriginalFilename())
                                , savepath);
                        if (message != null) {
                            redirectAttributes.addFlashAttribute("item", goodsDTO);
                            redirectAttributes.addFlashAttribute("errors", message);
                            return "redirect:/goods/regist.do";
                        }
                    }
                }
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("item", goodsDTO);
            redirectAttributes.addFlashAttribute("errors", e.getMessage());
            return "redirect:/goods/regist.do";
        }


        return "redirect:/goods/view.do?idx=" + goodsDTO.getIdx();
    }

    @GetMapping("/view.do")
    public String viewGet(@RequestParam(required = true, defaultValue = "0") int idx, Model model, RedirectAttributes redirectAttributes) {
        if (idx <= 0) {
            redirectAttributes.addFlashAttribute("errors", "등록되지 않은 상품입니다.");
            return "redirect:/goods/list.do";
        }
        model.addAttribute("item", goodsService.view(idx));
        return "goods/view";
    }

    @GetMapping("/modify.do")
    public String modifyGet(HttpSession session, Model model, @RequestParam(required = false, defaultValue = "0") int idx, RedirectAttributes redirectAttributes) {

        if (idx <= 0) {
            redirectAttributes.addFlashAttribute("errors", "등록되지 않은 상품입니다.");
            return "redirect:/goods/list.do";
        }

        GoodsDTO goodsDTO = goodsService.view(idx);

        if(goodsDTO != null && (goodsDTO.getStatus().equals("N") || goodsDTO.getStatus().equals("D"))) {
            redirectAttributes.addFlashAttribute("errors", "수정 불가 상품입니다.(판매 완료/삭제된 상품)");
            return "redirect:/goods/view.do?idx=" + idx;
        }

        if (session.getAttribute("memberId") == null || !session.getAttribute("memberId").equals(goodsDTO.getMemberId())) {
            redirectAttributes.addFlashAttribute("errors", "수정 권한이 없습니다.");
            return "redirect:/goods/view.do?idx=" + idx;
        }

        model.addAttribute("categories", goodsService.codeList("goods"));
        model.addAttribute("item", goodsDTO);
        List<FileDTO> list = goodsService.fileListByBbsCodeAndRefIdx("07", idx);

        if(list.size() >= 1) {
            model.addAttribute("orgMainImage", list.remove(0));
        }
        model.addAttribute("images", list);
        return "goods/modify";

    }

    @PostMapping("/modify.do")
    public String modifyPost(@RequestParam(required = false, defaultValue = "0") int idx
            , @Valid GoodsDTO goodsDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes
            , HttpSession session
            , @RequestParam(required = false) String[] deleteFile
            , @RequestParam(required = false) MultipartFile mainImage
            , @RequestParam(required = false) MultipartFile[] detailImage) {

        if (idx <= 0) {
            redirectAttributes.addFlashAttribute("errors", "존재하지 않는 상품입니다.");
            return "redirect:/goods/list.do";
        }

        GoodsDTO orgDTO = goodsService.view(idx);
        if(orgDTO == null) {
            redirectAttributes.addFlashAttribute("errors", "존재하지 않는 상품입니다.");
            return "redirect:/goods/list.do";
        }

        if(!orgDTO.getMemberId().equals(session.getAttribute("memberId"))) {
            redirectAttributes.addFlashAttribute("errors", "수정 권한이 없습니다.");
            return "redirect:/goods/list.do";
        }

        if(orgDTO.getStatus().equals("N") || orgDTO.getStatus().equals("D")) {
            redirectAttributes.addFlashAttribute("errors", "수정 불가 상품입니다.(삭제 또는 판매완료)");
            return "redirect:/goods/list.do";
        }

        String savepath = session.getServletContext().getRealPath("/resources/image");
        goodsDTO.setMemberId(session.getAttribute("memberId").toString());
        String message = null;


        if (deleteFile != null && deleteFile.length > 0) {

            for (String filename : deleteFile) {
                if(filename.contains("_0.")) {
                    if(mainImage.getSize() == 0) {
                        redirectAttributes.addFlashAttribute("errors", "메인 이미지를 등록해주세요.");
                        return "redirect:/goods/modify.do?idx=" + idx;
                    }
                }
            }

            for (String filename : deleteFile) {
                File dfile = new File(savepath, filename);
                dfile.delete();
                goodsService.deleteFileByName(filename);
            }
        }


        try {
            if (mainImage != null && mainImage.getSize() > 0) {
                message = upload(mainImage, goodsDTO.getIdx()
                        , "goods_" + goodsDTO.getIdx() + "_0" + getExt(mainImage.getOriginalFilename())
                        , savepath);
            }


            if (message != null) {
                redirectAttributes.addFlashAttribute("errors", message);
            }

            if (detailImage != null && detailImage.length > 0) {
                for (MultipartFile detail : detailImage) {
                    if (detail.getSize() > 0) {
                        message = upload(detail, goodsDTO.getIdx()
                                , "goods_" + goodsDTO.getIdx() + "_z" + UUID.randomUUID() + getExt(detail.getOriginalFilename())
                                , savepath);
                        if (message != null) {
                            redirectAttributes.addFlashAttribute("errors", message);
                        }
                    }
                }
            }

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errors", e.getMessage());
            return "redirect:/goods/modify.do?idx=" + goodsDTO.getIdx();
        }

        if (message != null) {
            redirectAttributes.addFlashAttribute("errors", message);
            return "redirect:/goods/modify.do?idx=" + goodsDTO.getIdx();
        }

        message = goodsService.modifyGoodsInfo(goodsDTO);
        if (message != null) {
            redirectAttributes.addFlashAttribute("errors", message);
            return "redirect:/goods/modify.do?idx=" + goodsDTO.getIdx();
        }

        return "redirect:/goods/mygoods.do";
    }

    @GetMapping("/delete.do")
    public String deleteGet(HttpSession session, @RequestParam(defaultValue = "0") int idx
            , @RequestParam(required = false, defaultValue = "1") int page_no, RedirectAttributes redirectAttributes) {
        if (idx <= 0) {
            redirectAttributes.addFlashAttribute("errors", "존재하지 않는 상품입니다.");
        }

        String errors = goodsService.deleteGoods(GoodsDTO.builder().idx(idx).reservationId(null).memberId(session.getAttribute("memberId").toString()).build());

        redirectAttributes.addFlashAttribute("errors", errors);

        return "redirect:/goods/mygoods.do?page_no=" + page_no;
    }

    @GetMapping("/direct.do")
    public String directGet(HttpSession session, @RequestParam(defaultValue = "0") int idx
            , @RequestParam(required = false, defaultValue = "1") int page_no, RedirectAttributes redirectAttributes) {

        if (idx <= 0) {
            redirectAttributes.addFlashAttribute("errors", "존재하지 않는 상품입니다.");
            return "redirect:/goods/mygoods.do?page_no=" + page_no;
        }

        String errors = goodsService.direct(GoodsDTO.builder().idx(idx).memberId(session.getAttribute("memberId").toString()).build());

        redirectAttributes.addFlashAttribute("errors", errors);

        return "redirect:/goods/mygoods.do?page_no=" + page_no;
    }

    @GetMapping("/cancel.do")
    public String cancelGet(HttpSession session, @RequestParam(defaultValue = "0") int idx
            , @RequestParam(required = false, defaultValue = "1") int page_no, RedirectAttributes redirectAttributes) {

        if (idx <= 0) {
            redirectAttributes.addFlashAttribute("errors", "존재하지 않는 상품입니다.");
            return "redirect:/goods/mygoods.do?page_no=" + page_no;
        }

        String errors = goodsService.cancelReservation(GoodsDTO.builder().memberId(session.getAttribute("memberId").toString()).idx(idx).build());
        String[] result = errors.split("::");
        redirectAttributes.addFlashAttribute("errors", result[0]);
        if(result.length > 1) {
            alertService.regist(AlertDTO.builder()
                    .memberId(result[1])
                    .content(result[2] + " 상품의 예약이 취소되었습니다.")
                    .build());
        }
        return "redirect:/goods/mygoods.do?page_no=" + page_no;
    }

    @GetMapping("/mygoods.do")
    public String mygoods(HttpSession session, Model model, @Valid PageRequestDTO pageRequestDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/goods/mygoods.do";
        }

        pageRequestDTO.setMemberId(session.getAttribute("memberId").toString());

        List<String> status = new ArrayList<>();
        status.add("Y");
        status.add("R");
        status.add("N");
        pageRequestDTO.setStatus(status);

        model.addAttribute("pageinfo", goodsService.listWithPayInfo(pageRequestDTO));
        model.addAttribute("categories", goodsService.codeList("goods"));
        return "goods/mygoods";
    }

    @GetMapping("/reservation.do")
    public String reservationGet(HttpSession session, Model model, @Valid PageRequestDTO pageRequestDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/goods/mygoods.do";
        }

        List<String> status = new ArrayList<>();
        status.add("R");
        pageRequestDTO.setStatus(status);

        pageRequestDTO.setReservationId(session.getAttribute("memberId").toString());

        model.addAttribute("pageinfo", goodsService.listByPage(pageRequestDTO));
        return "goods/reservation";
    }

    private String uploadFile(String orgName, String newName, byte[] fileData, String savePath) throws Exception {
        String saveName = newName;
        File targetFile = new File(savePath, saveName);
        FileCopyUtils.copy(fileData, targetFile);
        return saveName;
    }

    private String upload(MultipartFile file, int refIdx, String name, String savePath) throws Exception {
        String message = null;
        String newName = "";
        try {
            if (file != null && !file.isEmpty()) {
                FileDTO dto = new FileDTO();
                dto.setRefIdx(refIdx);
                dto.setFilePath(savePath);
                //dto.setFileName(file.getOriginalFilename());
                newName = uploadFile(file.getOriginalFilename(), name, file.getBytes(), savePath);
                dto.setFileName(newName);
                dto.setFileExt(Objects.requireNonNull(file.getOriginalFilename()).substring(file.getOriginalFilename().lastIndexOf(".")));
                dto.setFileContentType(file.getContentType());
                dto.setFileSize(file.getSize());
                dto.setOrgFileName(file.getOriginalFilename());
                dto.setBbsCode("07");

//                파일 데이터 db에 저장하는 코드
//                dto.setFileData(file.getBytes());

                message = goodsService.fileupload(dto);
            }
        } catch (Exception e) {
            log.info(e.getMessage());
            message = e.getMessage();
        }
        return message;
    }

    String getExt(String filename) {
        return Objects.requireNonNull(filename.substring(filename.lastIndexOf(".")));
    }
}
