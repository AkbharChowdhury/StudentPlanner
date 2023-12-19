package com.studentplanner.studentplanner.models;

import java.util.List;
import java.util.stream.Collectors;

public record ModuleTeacher(int moduleID, List<Integer> teacherIDList) {

    public static List<ModuleTeacher> filterModuleTeachers(List<ModuleTeacher> moduleTeachers, List<Integer> filteredModuleIdList) {
        return moduleTeachers.stream()
                .filter(p -> filteredModuleIdList.contains(p.moduleID()))
                .collect(Collectors.toList());
    }
    public static List<String> getTeacherNames(List<Teacher> teachers) {
        return teachers.stream().map(User::getName).toList();
    }

}