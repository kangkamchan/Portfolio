package net.fullstack7.swc.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class FriendDTO {
    private Integer friendId;
    private String receiver; // 수락할사람
    private String receiverName;
    private String requester; // 신청자
    private String requesterName;
    private Integer status;
    private LocalDateTime regDate;
}
