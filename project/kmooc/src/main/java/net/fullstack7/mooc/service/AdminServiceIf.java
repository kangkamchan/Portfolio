package net.fullstack7.mooc.service;

import net.fullstack7.mooc.domain.Course;
import net.fullstack7.mooc.domain.Member;
import net.fullstack7.mooc.domain.Notice;
import net.fullstack7.mooc.domain.Teacher;
import net.fullstack7.mooc.dto.*;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface AdminServiceIf {
    boolean existsAdmin(String id);

    String login(AdminLoginDTO adminLoginDTO);

    Page<Teacher> getTeachers(AdminSearchDTO adminSearchDTO);
    Page<Member> getMembers(AdminSearchDTO adminSearchDTO);
    String modifyMemberStatus(String type, String userId);
    String approveTeacherRegist(String teacherId);
    Teacher getTeacher(String id);
    Member getMember(String id);
    String deleteMember(String id, String typeSelect);

    Page<CourseResponseDTO> getCourses(CourseSearchDTO searchDTO);
    Course getCourse(int id);
    String modifyCourseStatus(String type, int courseId);

    String insertNotice(NoticeDTO dto);
    String modifyNotice(NoticeDTO dto);
    String deleteNotice(int noticeId, String adminId);

    Map<String, Integer> mainPageInfo();
}
