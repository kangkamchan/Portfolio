package net.fullstack7.mooc.service;

import net.fullstack7.mooc.domain.Notice;
import net.fullstack7.mooc.dto.NoticeDTO;
import net.fullstack7.mooc.dto.PageDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface NoticeServiceIf {
    Page<NoticeDTO> getNotices(PageDTO<NoticeDTO> pageDTO);
    Notice view(int noticeId);
    List<NoticeDTO> getNewestNotices();
}
