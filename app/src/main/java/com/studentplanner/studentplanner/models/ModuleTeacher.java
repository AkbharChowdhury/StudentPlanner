package com.studentplanner.studentplanner.models;

import android.widget.ListView;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public record ModuleTeacher(int moduleID, List<Integer> teacherIDList) {

    public static List<ModuleTeacher> filterModuleTeachers(List<ModuleTeacher> moduleTeachers, List<Integer> filteredModuleIdList) {
        return moduleTeachers.stream()
                .filter(p -> filteredModuleIdList.contains(p.moduleID()))
                .collect(Collectors.toList());
    }
    public static List<String> getTeacherNames(List<Teacher> teachers) {
        return teachers.stream().map(User::getName).toList();
    }
    public static List<Integer> getSelectedTeacherIDList(List<Teacher> teacherList, ListView listView){
        int[] selectedTeacherIds = IntStream.range(0, listView.getCount())
                .filter(listView::isItemChecked)
                .map(i ->  teacherList.get(i).getUserID())
                .toArray();

        return Arrays.stream(selectedTeacherIds).boxed().toList();
    }

}