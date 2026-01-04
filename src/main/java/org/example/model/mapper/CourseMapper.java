package org.example.model.mapper;

import org.example.model.dto.CourseInfo;
import org.example.model.entity.CourseEntity;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public final class CourseMapper {

    private CourseMapper() {}

    public static CourseInfo toCourseInfo(CourseEntity course) {
        if (course == null) return null;
        return new CourseInfo(course.getId(), course.getName(), course.getEcts(), course.getSemester());
    }

    public static List<CourseInfo> toCourseInfoList(Collection<CourseEntity> entities) {
        if (entities == null || entities.isEmpty()) return List.of();
        return entities.stream()
                .filter(Objects::nonNull)
                .map(CourseMapper::toCourseInfo)
                .collect(Collectors.toList());
    }
}