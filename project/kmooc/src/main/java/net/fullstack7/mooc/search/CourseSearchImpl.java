package net.fullstack7.mooc.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import net.fullstack7.mooc.domain.*;
import net.fullstack7.mooc.dto.CourseResponseDTO;
import net.fullstack7.mooc.dto.CourseSearchDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.Objects;

@Log4j2
public class CourseSearchImpl extends QuerydslRepositorySupport implements CourseSearch {
    public CourseSearchImpl() {
        super(Course.class);
    }

    @Override
    public Page<CourseResponseDTO> coursePage(Pageable pageable, CourseSearchDTO courseSearchDTO, String memberId, int isCompleted) {

        QCourse qCourse = QCourse.course;

        JPQLQuery<Course> query = from(qCourse);

        BooleanBuilder builder = new BooleanBuilder();

        if (memberId != null && !memberId.isEmpty()) {
            QCourseEnrollment qCourseEnrollment = QCourseEnrollment.courseEnrollment;
            query.innerJoin(qCourseEnrollment).on(qCourseEnrollment.course.eq(qCourse));
            builder.and(qCourseEnrollment.member.memberId.eq(memberId));

            if (isCompleted == 0)
                builder.and(qCourseEnrollment.isCompleted.eq(0));
            else if (isCompleted == 1)
                builder.and(qCourseEnrollment.isCompleted.eq(1));
        }


        //학점은행제
        if (courseSearchDTO.getIsCreditBank() > -1)
            builder.and(qCourse.isCreditBank.eq(courseSearchDTO.getIsCreditBank()));
        //상태
        if (courseSearchDTO.getStatus() != null && !courseSearchDTO.getStatus().isEmpty())
            builder.and(qCourse.status.eq(courseSearchDTO.getStatus()));
        //과목
        if (courseSearchDTO.getSubjectId() > -1)
            builder.and(qCourse.subject.subjectId.eq(courseSearchDTO.getSubjectId()));
        //기관
        if (courseSearchDTO.getInstitutionId() > -1)
            builder.and(qCourse.teacher.institution.institutionId.eq(courseSearchDTO.getInstitutionId()));

//        검색필드 여러개일때
//        if (courseSearchDTO.getSearchField() != null && courseSearchDTO.getSearchValue() != null && !courseSearchDTO.getSearchValue().isEmpty()) {
//            switch (courseSearchDTO.getSearchField()) {
//                case "title":
//                    builder.and(qCourse.title.contains(courseSearchDTO.getSearchValue()));
//                    break;
//                //케이스 추가
//            }
//        }

//        제목검색만 있을 때
        if (courseSearchDTO.getSearchValue() != null && !courseSearchDTO.getSearchValue().isEmpty()) {
            builder.and(qCourse.title.contains(courseSearchDTO.getSearchValue()));
        }

        if (builder.hasValue())
            query.where(builder);

        if (courseSearchDTO.getSortField() != null && !courseSearchDTO.getSortField().isEmpty()) {
            switch (courseSearchDTO.getSortField()) {
//                기본순, 최신등록순, 가나다순
                case "title":
                    query.orderBy(qCourse.title.asc());
                    break;
                case "createdAt":
                    query.orderBy(qCourse.createdAt.desc());
                    break;
            }
        }

        Objects.requireNonNull(this.getQuerydsl()).applyPagination(pageable, query);
        List<Course> courses = query.fetch();

        List<CourseResponseDTO> courses2 = courses.stream().map(entity -> CourseResponseDTO.builder()
                .courseId(entity.getCourseId())
                .isCreditBank(entity.getIsCreditBank())
                .title(entity.getTitle())
                .weeks(entity.getWeeks())
                .thumbnail(entity.getThumbnail())
                .learningTime(entity.getLearningTime())
                .description(entity.getDescription())
                .language(entity.getLanguage())
                .status(entity.getStatus())
                .teacherName(entity.getTeacher().getTeacherName())
                .institutionName(entity.getTeacher().getInstitution().getInstitutionName())
                .createdAt(entity.getCreatedAt())
                .build()
        ).toList();

        int total = (int) query.fetchCount();

        return new PageImpl<>(courses2, pageable, total);
    }

    @Override
    public List<CourseResponseDTO> randomCourses(int n) {
        QCourse qCourse = QCourse.course;

        JPQLQuery<Course> query = from(qCourse);

        BooleanBuilder builder = new BooleanBuilder();

        builder.and(qCourse.status.eq("PUBLISHED"));

        if (builder.hasValue()) {
            query.where(builder);
        }

        query.orderBy(qCourse.viewCount.desc());
        query.limit(n);

        List<Course> courses = query.fetch();

        return courses.stream().map(entity -> CourseResponseDTO.builder()
               .courseId(entity.getCourseId())
               .isCreditBank(entity.getIsCreditBank())
               .title(entity.getTitle())
               .weeks(entity.getWeeks())
               .thumbnail(entity.getThumbnail())
               .learningTime(entity.getLearningTime())
               .description(entity.getDescription())
               .language(entity.getLanguage())
               .status(entity.getStatus())
               .teacherName(entity.getTeacher().getTeacherName())
               .institutionName(entity.getTeacher().getInstitution().getInstitutionName())
               .createdAt(entity.getCreatedAt())
               .build()
        ).toList();
    }


}
