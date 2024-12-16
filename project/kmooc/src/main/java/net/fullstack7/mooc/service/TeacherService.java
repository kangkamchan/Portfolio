package net.fullstack7.mooc.service;

import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import net.fullstack7.mooc.dto.TeacherJoinDTO;
import net.fullstack7.mooc.repository.TeacherRepository;
import net.fullstack7.mooc.domain.Institution;
import net.fullstack7.mooc.domain.Teacher;
import net.fullstack7.mooc.repository.InstitutionRepository;
import net.fullstack7.mooc.domain.Course;
import net.fullstack7.mooc.repository.CourseRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class TeacherService {
    
    private final TeacherRepository teacherRepository;
    private final InstitutionRepository institutionRepository;
    private final CourseRepository courseRepository;
    
    public void join(TeacherJoinDTO dto) {
        // 아이디 중복 체크
        if (teacherRepository.existsByTeacherId(dto.getTeacherId())) {
            throw new IllegalArgumentException("이미 사용중인 아이디입니다.");
        }
        
        // 이메일 중복 체크
        if (teacherRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("이미 사용중인 이메일입니다.");
        }
        
        // 소속기관 존재 여부 체크
        Institution institution = institutionRepository.findByInstitutionId(dto.getInstitutionId())
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 소속기관입니다."));

        Teacher teacher = Teacher.builder()
                .teacherId(dto.getTeacherId())
                .password(dto.getPassword())
                .teacherName(dto.getTeacherName())
                .email(dto.getEmail())
                .institution(institution)
                .isApproved(0)
                .status("INACTIVE")
                .createdAt(LocalDateTime.now())
                .build();
                
        teacherRepository.save(teacher);
    }
    
    public Teacher login(String teacherId, String password) {
        return teacherRepository.findByTeacherIdAndPassword(teacherId, password)
            .orElseThrow(() -> new IllegalArgumentException("아이디 또는 비밀번호가 일치하지 않습니다."));
    }

    public Teacher getTeacher(String teacherId) {
        return teacherRepository.findByTeacherId(teacherId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 교사입니다."));
    }

    public boolean checkId(String teacherId) {
        return teacherRepository.existsByTeacherId(teacherId);
    }

    public boolean checkEmail(String email) {
        return teacherRepository.existsByEmail(email);
    }

    @Transactional(readOnly = true)
    public List<Course> getMyLectures(String teacherId) {
        Teacher teacher = teacherRepository.findByTeacherId(teacherId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 교사입니다."));
        return courseRepository.findByTeacherOrderByCreatedAtDesc(teacher);
    }
}
