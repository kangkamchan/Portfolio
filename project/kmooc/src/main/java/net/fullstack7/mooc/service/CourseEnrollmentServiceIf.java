package net.fullstack7.mooc.service;

import net.fullstack7.mooc.dto.CourseEnrollmentDTO;
import net.fullstack7.mooc.domain.Member;

public interface CourseEnrollmentServiceIf {
    public int regist(CourseEnrollmentDTO courseEnrollmentDTO);
    CourseEnrollmentDTO isEnrolled(CourseEnrollmentDTO courseEnrollmentDTO);
    public String delete(CourseEnrollmentDTO courseEnrollmentDTO);
    public boolean checkAuthority(String memberId, int courseId);
    public int updateCourseEnrollment(int courseId, String memberId);
    public boolean isAlreadyCompleted(int courseId, String memberId);
}
