package com.studentplanner.studentplanner.models;

import java.util.List;
import java.util.stream.Collectors;

public class ModuleTeacher  {
    private final int moduleID;
    private final List<Integer> teacherIDList;

    public ModuleTeacher(int moduleID, List<Integer> teacherIDList) {
        this.moduleID = moduleID;
        this.teacherIDList = teacherIDList;
    }

    public List<Integer> getTeacherIDList() {
        return teacherIDList;
    }

    public int getModuleID() {
        return moduleID;
    }


    public static List<ModuleTeacher> filterModuleTeachers(final List<ModuleTeacher> ALL, final List<Integer> moduleIdList){
        return ALL.stream()
                .filter(p -> moduleIdList.contains(p.getModuleID()))
                .collect(Collectors.toList());
    }
}