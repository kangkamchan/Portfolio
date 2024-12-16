package net.fullstack7.mooc.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.fullstack7.mooc.domain.Course;
import net.fullstack7.mooc.domain.CourseEnrollment;
import net.fullstack7.mooc.dto.CourseEnrollmentDTO;
import net.fullstack7.mooc.repository.CourseEnrollmentRepository;
import net.fullstack7.mooc.repository.CourseRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import net.fullstack7.mooc.domain.Member;
import net.fullstack7.mooc.repository.MemberRepository;

@Log4j2
@Service
@RequiredArgsConstructor
@Transactional
public class CourseEnrollmentServiceImpl implements CourseEnrollmentServiceIf {
    private final CourseEnrollmentRepository courseEnrollmentRepository;
    private final CourseRepository courseRepository;
    private final MemberRepository memberRepository;
    private final ModelMapper modelMapper;

    @Override
    public int regist(CourseEnrollmentDTO courseEnrollmentDTO) {
        Course course = courseRepository.findById(courseEnrollmentDTO.getCourse().getCourseId()).orElseThrow(() -> new IllegalArgumentException("강의가 없음"));
        if(!course.getStatus().equals("PUBLISHED")){
            throw new IllegalArgumentException("삭제 또는 준비중인 강의");
        }
        CourseEnrollment courseEnrollment = courseEnrollmentRepository.findByCourseAndMember(courseEnrollmentDTO.getCourse(),courseEnrollmentDTO.getMember()).orElse(null);
        if(courseEnrollment != null) {
            throw new IllegalArgumentException("이미 수강중인 강의");
        }
        if(course.getIsCreditBank()==1){
            if(courseEnrollmentDTO.getMember().getMemberType()==0){
                throw new IllegalArgumentException("학점은행제 회원이 아님");
            }
        }
        return courseEnrollmentRepository.save(CourseEnrollment.builder().course(course).member(courseEnrollmentDTO.getMember()).enrollmentDate(courseEnrollmentDTO.getEnrollmentDate()).build()).getCourseEnrollmentId();
    }

    @Override
    public CourseEnrollmentDTO isEnrolled(CourseEnrollmentDTO courseEnrollmentDTO) {
        CourseEnrollment courseEnrollment = courseEnrollmentRepository.findByCourseAndMember(courseEnrollmentDTO.getCourse(),courseEnrollmentDTO.getMember()).orElse(null);
        if(courseEnrollment == null) {
            return null;
        }
        return modelMapper.map(courseEnrollment, CourseEnrollmentDTO.class);
    }

    @Override
    public String delete(CourseEnrollmentDTO courseEnrollmentDTO) {
        CourseEnrollment courseEnrollment = courseEnrollmentRepository.findByCourseAndMember(courseEnrollmentDTO.getCourse(),courseEnrollmentDTO.getMember()).orElse(null);
        if(courseEnrollment == null) {
            return "수강중인 강의가 아닙니다.";
        }
        courseEnrollmentRepository.delete(courseEnrollment);
        if(courseEnrollmentRepository.existsById(courseEnrollment.getCourseEnrollmentId())) {
            return "수강 취소 실패";
        }
        return null;
    }

    @Override
    public boolean checkAuthority(String memberId, int courseId) {
        Member member = memberRepository.findByMemberId(memberId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 강의입니다."));
        // 수강중인 강의인지 확인
        return courseEnrollmentRepository.findByCourseAndMember(course, member).isPresent();
    }

    @Override
    public int updateCourseEnrollment(int courseId, String memberId) {
        try{
            Member member = memberRepository.findByMemberId(memberId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
            Course course = courseRepository.findById(courseId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 강의입니다."));
            CourseEnrollment courseEnrollment = courseEnrollmentRepository.findByCourseAndMember(course, member).orElseThrow(() -> new IllegalArgumentException("수강중인 강의가 아닙니다."));
            courseEnrollment.setIsCompleted(1);
            return courseEnrollmentRepository.updateCourseEnrollment(course, member);
        }catch(Exception e){
            return 0;
        }
    }

    @Override
    public boolean isAlreadyCompleted(int courseId, String memberId) {
        try {
            Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
            Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 강의입니다."));
            CourseEnrollment enrollment = courseEnrollmentRepository.findByCourseAndMember(course, member)
                .orElseThrow(() -> new IllegalArgumentException("수강중인 강의가 아닙니다."));
                
            return enrollment.getIsCompleted() == 1;
        } catch(Exception e) {
            return false;
        }
    }

}
