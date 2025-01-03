package net.fullstack7.swc.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.fullstack7.swc.domain.Qna;
import net.fullstack7.swc.dto.AdminQnaDTO;
import net.fullstack7.swc.dto.QnaDTO;
import net.fullstack7.swc.repository.AdminRepository;
import net.fullstack7.swc.repository.QnaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class QnaServiceImpl implements QnaServiceIf {
    private final QnaRepository qnaRepository;
    private final JavaMailSender mailSender; // 의존성 주입 필요

    @Override
    public Integer registQna(QnaDTO qnaDTO) {
        Qna qna = new Qna(
                qnaDTO.getTitle(),
                qnaDTO.getContent(),
                qnaDTO.getEmail(),
                qnaDTO.getPassword(),
                qnaDTO.getRegDate()
        );

        Qna savedQna = qnaRepository.save(qna);
        return savedQna.getQnaId();
    }

    @Override
    @Transactional(readOnly = true)
    public QnaDTO viewQna(Integer qnaId, String password,boolean isAdmin) {
        Qna qna = qnaRepository.findById(qnaId)
                .orElseThrow(() -> new IllegalArgumentException("해당 QnA가 존재하지 않습니다."));

        if (!isAdmin) {
            if (qna.getPassword() != null && !qna.getPassword().equals(password)) {
                throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
            }
        }

        return convertToDTO(qna);
    }

    @Override
    public void addReply(AdminQnaDTO adminQnaDTO, boolean isAdmin) {
        if (!isAdmin) {
            throw new SecurityException("관리자만 답변할 수 있습니다.");
        }

        Qna parent = qnaRepository.findById(adminQnaDTO.getParentId())
                .orElseThrow(() -> new IllegalArgumentException("원글이 존재하지 않습니다."));

        Qna reply = new Qna(
                adminQnaDTO.getTitle(),
                adminQnaDTO.getContent(),
                parent.getEmail(),
                parent.getPassword(),
                parent.getRegDate());

        parent.addReply(reply);

        qnaRepository.save(parent);

        // 이메일 발송( 집에서 확인 )
        if (parent.getEmail() != null && !parent.getEmail().isEmpty()) {
            sendMail(parent.getEmail(), parent.getTitle(), reply.getContent());
        }
    }

    @Override
    public void deleteQna(Integer qnaId, String password, boolean isAdmin) {
        Qna qna = qnaRepository.findById(qnaId)
                .orElseThrow(() -> new IllegalArgumentException("QnA가 존재하지 않습니다."));

        if (!isAdmin) {
            // 비밀번호 검증
            if (qna.getPassword() == null || !qna.getPassword().equals(password)) {
                throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
            }
        }

        qnaRepository.delete(qna);
    }

    @Override
    public List<QnaDTO> listQna() {
        List<Qna> rootQnaList = qnaRepository.findAllRootQna();

        return rootQnaList.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public QnaDTO adminViewQna(Integer qnaId) {
        Qna qna = qnaRepository.findById(qnaId)
                .orElseThrow(() -> new IllegalArgumentException("해당 QnA가 존재하지 않습니다."));
        return convertToDTO(qna);
    }

    // Qna 엔티티를 QnaDTO로 변환
    private QnaDTO convertToDTO(Qna qna) {
        List<QnaDTO> replies = qna.getReplies().stream()
                .map(reply -> QnaDTO.builder()
                        .qnaId(reply.getQnaId())
                        .title(reply.getTitle())
                        .content(reply.getContent())
                        .answered(reply.isAnswered())
                        .regDate(reply.getRegDate())
                        .parentId(reply.getParent().getQnaId())
                        .build())
                .collect(Collectors.toList());

        return QnaDTO.builder()
                .qnaId(qna.getQnaId())
                .title(qna.getTitle())
                .content(qna.getContent())
                .answered(qna.isAnswered())
                .email(qna.getEmail())
                .replies(replies)
                .regDate(qna.getRegDate())
                .build();
    }

    // 이메일 발송 메서드
    private void sendMail(String toEmail, String subject, String answerContent) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("SWC QnA 답변 : " + subject);
        message.setText("답변 내용:\n" + answerContent);
        mailSender.send(message);
    }

    //페이징
    @Override
    @Transactional(readOnly = true)
    public Page<QnaDTO> listQnaPage(Pageable pageable) {
        Page<Qna> qnaPage = qnaRepository.findByParentIsNull(pageable);
        log.info("서비스에서 리스트 확인: {}", qnaPage.getContent());
        return qnaPage.map(this::convertToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<QnaDTO> listQnaByAnsweredPage(Pageable pageable, boolean answered) {
        Page<Qna> qnaPage = qnaRepository.findByAnswered(pageable, answered);
        log.info("서비스에서 리스트 확인 (answered={}): {}", answered, qnaPage.getContent());
        return qnaPage.map(this::convertToDTO);
    }
}
