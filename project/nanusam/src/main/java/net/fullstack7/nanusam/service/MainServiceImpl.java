package net.fullstack7.nanusam.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.fullstack7.nanusam.domain.GoodsVO;
import net.fullstack7.nanusam.dto.GoodsDTO;
import net.fullstack7.nanusam.mapper.MainMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class MainServiceImpl implements MainService {
  private final MainMapper mainXmlMapper;
  private final ModelMapper modelmapper;
  private static final int PAGE_SIZE = 10;

  // 메인 페이지 상품 리스트
  @Override
  public List<GoodsDTO> mainViewGoodsList(int page) {
    int offset = (page - 1) * PAGE_SIZE;
    List<GoodsVO> voList = mainXmlMapper.mainViewGoodsList(offset, PAGE_SIZE);
    return voList.stream()
        .map(vo -> modelmapper.map(vo, GoodsDTO.class))
        .collect(Collectors.toList());
  }
}
