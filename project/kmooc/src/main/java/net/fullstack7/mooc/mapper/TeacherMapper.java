package net.fullstack7.mooc.mapper;

import net.fullstack7.mooc.domain.Teacher;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TeacherMapper {
    List<Teacher> teacherList();
}
