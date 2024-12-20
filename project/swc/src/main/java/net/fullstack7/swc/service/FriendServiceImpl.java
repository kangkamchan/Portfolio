package net.fullstack7.swc.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.fullstack7.swc.domain.AlertType;
import net.fullstack7.swc.domain.Friend;
import net.fullstack7.swc.domain.Member;
import net.fullstack7.swc.dto.*;
import net.fullstack7.swc.mapper.FriendMapper;
import net.fullstack7.swc.repository.FriendRepository;
import net.fullstack7.swc.repository.MemberRepository;
import net.fullstack7.swc.util.LogUtil;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendServiceIf {
    private final FriendRepository friendRepository;
    private final MemberRepository memberRepository;
    private final AlertServiceIf alertService; // 알림 기능 통합 시 사용

    //강감찬추가
    private final FriendMapper friendMapper;
    private final ModelMapper modelMapper;
    //강감찬추가

    @Override
    @Transactional
    public void sendFriendRequest(String requesterId, FriendDTO friendDTO) {
        // 본인에게 금지( 테스트 후 살리기)
        if (requesterId.equals(friendDTO.getReceiver())) {
            throw new RuntimeException("본인에게 친구 요청을 할 수 없습니다.");
        }
        Member requester = memberRepository.findById(requesterId)
                .orElseThrow(() -> new RuntimeException("요청자 회원이 존재하지 않습니다."));
        Member receiver = memberRepository.findById(friendDTO.getReceiver())
                .orElseThrow(() -> new RuntimeException("수신자 회원이 존재하지 않습니다."));

        Optional<Friend> existingFriend = friendRepository.findByRequesterAndReceiver(requester, receiver);
        if (existingFriend.isPresent()) {
            throw new RuntimeException("이미 친구 요청을 보냈거나 친구 관계입니다.");
        }

        existingFriend = friendRepository.findByRequesterAndReceiver(receiver, requester);
        if (existingFriend.isPresent()) {
            throw new RuntimeException("이미 친구 요청을 보냈거나 친구 관계입니다.");
        }

        Friend friend = new Friend(receiver, requester, 0, LocalDateTime.now());
        friendRepository.save(friend);

         String message = requester.getName() + "님이 친구 요청을 보냈습니다.";
        alertService.registAlert(receiver, AlertType.FRIEND_REQUEST, message, "/myPage/followList");
    }

    @Override
    @Transactional
    public void acceptFriendRequest(Integer friendId, String receiverId) {
        Friend friend = friendRepository.findById(friendId)
                .orElseThrow(() -> new RuntimeException("친구 요청이 존재하지 않습니다."));
        if (!friend.getReceiver().getMemberId().equals(receiverId)) {
            throw new RuntimeException("권한이 없습니다.");
        }
        if (friend.getStatus() != 0) {
            throw new RuntimeException("이미 처리된 친구 요청입니다.");
        }

        friend.allow();
        friendRepository.save(friend);
        String memberName = friend.getReceiver().getName();

         String message = memberName + "님이 친구 요청을 수락했습니다.";
        alertService.registAlert(friend.getRequester(), AlertType.FRIEND_ACCEPTED, message, "/myPage/friend");
    }

    @Override
    @Transactional
    public void rejectFriendRequest(Integer friendId, String receiverId) {
        Friend friend = friendRepository.findById(friendId)
                .orElseThrow(() -> new RuntimeException("친구 요청이 존재하지 않습니다."));
        if (!friend.getReceiver().getMemberId().equals(receiverId)) {
            throw new RuntimeException("권한이 없습니다.");
        }
        if (friend.getStatus() != 0) { // 상태가 대기인지 확인
            throw new RuntimeException("이미 처리된 친구 요청입니다.");
        }

        friendRepository.delete(friend);

    }

    @Override
    @Transactional
    public void deleteFriend(Integer friendId, String memberId) {
        Friend friend = friendRepository.findById(friendId)
                .orElseThrow(() -> new RuntimeException("친구 관계가 존재하지 않습니다."));
        if (!friend.getRequester().getMemberId().equals(memberId) &&
                !friend.getReceiver().getMemberId().equals(memberId)) {
            throw new RuntimeException("권한이 없습니다.");
        }

        friendRepository.delete(friend);

    }

    @Override
    @Transactional(readOnly = true)
    public List<FriendDTO> getFriendRequests(String receiverId) {
        Member receiver = memberRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("회원이 존재하지 않습니다."));
        List<Friend> pendingFriends = friendRepository.findByReceiverAndStatus(receiver, 0);
        return pendingFriends.stream().map(friend -> {
            FriendDTO dto = new FriendDTO();
            dto.setFriendId(friend.getFriendId());
            dto.setRequester(friend.getRequester().getMemberId());
            dto.setRequesterName(friend.getRequester().getName());
            dto.setReceiver(friend.getReceiver().getMemberId());
            dto.setReceiverName(friend.getReceiver().getName());
            dto.setStatus(friend.getStatus());
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<FriendDTO> getFriends(String memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("회원이 존재하지 않습니다."));
        List<Friend> friends = friendRepository.findByRequesterOrReceiver(
                member, member);
        return friends.stream().map(friend -> {
            FriendDTO dto = new FriendDTO();
            dto.setFriendId(friend.getFriendId());
            dto.setRequester(friend.getRequester().getMemberId());
            dto.setRequesterName(friend.getRequester().getName());
            dto.setReceiver(friend.getReceiver().getMemberId());
            dto.setReceiverName(friend.getReceiver().getName());
            dto.setStatus(friend.getStatus());
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MemberDTO> searchFriends(String keyword, int limit, int page) {
        Pageable pageable = PageRequest.of(page, limit);
        return memberRepository.findById(keyword, pageable).stream().map(member -> modelMapper.map(member, MemberDTO.class)).collect(Collectors.toList());
    }

    //강감찬 추가
    @Override
    public List<FriendShareDTO> notSharedFriends(Integer postId,String memberId) {
        return friendRepository.findNotSharedFriends(postId, memberId).stream().map(f -> modelMapper.map(f,FriendShareDTO.class)).toList();
    }
    @Override
    public PageDTO<FriendListDTO> getFriendList(PageDTO<FriendListDTO> pageDTO, String memberId) {
        LogUtil.logLine("FriendService getFriendList");
        try{
            pageDTO.setDtoList(friendMapper.friendList(pageDTO,memberId));
            return pageDTO;
        }catch(Exception e){
            log.error(e.getMessage());
            return null;
        }
    }
    @Override
    public int getTotalCount(PageDTO<FriendListDTO> pageDTO, String memberId) {
        LogUtil.logLine("FriendService getTotalCount");
        try{
            return friendMapper.totalCount(pageDTO,memberId);
        }catch(Exception e){
            log.error(e.getMessage());
            return -1;
        }
    }

    @Override
    public PageDTO<FriendListDTO> getFriendRequestList(PageDTO<FriendListDTO> pageDTO, String memberId) {
        LogUtil.logLine("FriendService getFriendRequestList");
        try{
            pageDTO.setDtoList(friendMapper.friendRequestList(pageDTO,memberId));
            return pageDTO;
        }catch(Exception e){
            log.error(e.getMessage());
            return null;
        }
    }

    @Override
    public int getRequestTotalCount(PageDTO<FriendListDTO> pageDTO, String memberId) {
        LogUtil.logLine("FriendService getRequestTotalCount");
        try{
            return friendMapper.requestTotalCount(pageDTO,memberId);
        }catch(Exception e){
            log.error(e.getMessage());
            return -1;
        }
    }
    //강감찬 추가

}
