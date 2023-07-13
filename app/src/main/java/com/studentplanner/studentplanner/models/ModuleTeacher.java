package com.studentplanner.studentplanner.models;

import java.util.List;
import java.util.stream.Collectors;

public record ModuleTeacher(int moduleID, List<Integer> teacherIDList) {

    public static List<ModuleTeacher> filterModuleTeachers(final List<ModuleTeacher> ALL, final List<Integer> filteredModuleIdList) {
        return ALL.stream()
                .filter(p -> filteredModuleIdList.contains(p.moduleID()))
                .collect(Collectors.toList());
    }
}