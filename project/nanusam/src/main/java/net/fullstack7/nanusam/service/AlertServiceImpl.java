package net.fullstack7.nanusam.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.fullstack7.nanusam.domain.AlertVO;
import net.fullstack7.nanusam.dto.AlertDTO;
import net.fullstack7.nanusam.dto.PageRequestDTO;
import net.fullstack7.nanusam.dto.PageResponseDTO;
import net.fullstack7.nanusam.mapper.AlertMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class AlertServiceImpl implements AlertService {
    private final AlertMapper alertMapper;
    private final ModelMapper modelMapper;

    @Override
    public int regist(AlertDTO dto) {
        return alertMapper.regist(modelMapper.map(dto, AlertVO.class));
    }

    @Override
    public PageResponseDTO<AlertDTO> listWithPage(String memberId, PageRequestDTO dto) {
        return PageResponseDTO.<AlertDTO>withAll()
                .reqDTO(dto)
                .dtoList(alertMapper.listWithPage(memberId,dto).stream().map(vo->modelMapper.map(vo, AlertDTO.class)).collect(Collectors.toList()))
                .total_count(totalCount(memberId))
                .build();
    }

    @Override
    public int totalCount(String memberId) {
        return alertMapper.totalCount(memberId);
    }

    @Override
    public int modify(AlertDTO dto) {
        return alertMapper.modify(modelMapper.map(dto,AlertVO.class));
    }

    @Override
    public Integer unreadCount(String memberId) {
        return alertMapper.unreadCount(memberId)==null?0:alertMapper.unreadCount(memberId);
    }

    @Override
    public int modifyStatus(String memberId) {
        return alertMapper.modifyStatus(memberId);
    }
}
