package net.fullstack7.nanusam.dto;

import lombok.*;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartDTO {
    private int idx;
    private String memberId;
    private int goodsIdx;
    private String name;    //G.name
    private String mainImageName;
}
