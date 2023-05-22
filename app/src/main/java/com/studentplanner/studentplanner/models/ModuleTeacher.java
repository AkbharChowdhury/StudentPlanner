package com.studentplanner.studentplanner.models;

import java.util.ArrayList;
import java.util.List;

public class ModuleTeacher {
    private int moduleID;
    private int teacherID;

    public ModuleTeacher(int moduleID, List<Integer> teacherIDList) {
        this.moduleID = moduleID;
        this.teacherIDList = teacherIDList;
    }

    //    private int teacherID;
    private List<Integer> teacherIDList;


    public ModuleTeacher(int moduleID, int teacherID) {
        this.moduleID = moduleID;
        this.teacherID = teacherID;
    }

    public int getModuleID() {
        return moduleID;
    }

    public List<Integer> getTeacherIDList() {
        return teacherIDList;
    }

    public int getTeacherID() {
        return teacherID;
    }
}
