package net.fullstack7.mooc.search;

import net.fullstack7.mooc.domain.Notice;
import net.fullstack7.mooc.dto.NoticeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NoticeSearch {
    Page<NoticeDTO> noticePage(Pageable pageable, String searchType, String searchWord);
}
