package net.fullstack7.mooc.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import net.fullstack7.mooc.domain.QTeacher;
import net.fullstack7.mooc.domain.Teacher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.Objects;

public class TeacherSearchImpl extends QuerydslRepositorySupport implements TeacherSearch {
    public TeacherSearchImpl() {
        super(Teacher.class);
    }

    public Page<Teacher> adminTeacherPage(Pageable pageable, int isApproved, String status, String teacherId) {
        QTeacher teacherq = QTeacher.teacher;
        JPQLQuery<Teacher> query = from(teacherq);

        BooleanBuilder bb = new BooleanBuilder();

        if(isApproved != -1) {
            bb.and(teacherq.isApproved.eq(isApproved));
        }
        if(status != null) {
            bb.and(teacherq.status.eq(status));
        }
        if(teacherId != null) {
            bb.and(teacherq.teacherId.containsIgnoreCase(teacherId));
        }
        if(bb.hasValue()) query.where(bb);

        query.orderBy(teacherq.createdAt.desc());

        Objects.requireNonNull(this.getQuerydsl()).applyPagination(pageable, query);
        List<Teacher> teachers = query.fetch();

        int total = (int) query.fetchCount();

        return new PageImpl<>(teachers, pageable, total);
    }
}
