package net.fullstack7.mooc.search;

import net.fullstack7.mooc.domain.Teacher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TeacherSearch {
    Page<Teacher> adminTeacherPage(Pageable pageable, int isApproved, String status, String teacherId);
}
