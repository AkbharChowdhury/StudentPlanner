package com.studentplanner.studentplanner.models;

import com.google.android.material.textfield.TextInputLayout;

public class Classes {
    private int classID;
    private int moduleID;
    private int semesterID;
    private int dow;
    private String startTime;
    private String endTime;
    private String room;
    private String classType;

    private TextInputLayout txtDayError;
    private TextInputLayout txtSemesterError;
    private TextInputLayout txtModuleError;
    private TextInputLayout txtClassTypeError;

    public TextInputLayout getTxtDayError() {
        return txtDayError;
    }

    public TextInputLayout getTxtSemesterError() {
        return txtSemesterError;
    }

    public TextInputLayout getTxtModuleError() {
        return txtModuleError;
    }

    public TextInputLayout getTxtClassTypeError() {
        return txtClassTypeError;
    }

    public Classes(TextInputLayout txtDayError, TextInputLayout txtSemesterError, TextInputLayout txtModuleError, TextInputLayout txtClassTypeError) {
        this.txtDayError = txtDayError;
        this.txtSemesterError = txtSemesterError;
        this.txtModuleError = txtModuleError;
        this.txtClassTypeError = txtClassTypeError;
    }







    public Classes(int moduleID, int semesterID, int dow, String startTime, String endTime, String room, String classType) {
        this.moduleID = moduleID;
        this.semesterID = semesterID;
        this.dow = dow;
        this.startTime = startTime;
        this.endTime = endTime;
        this.room = room;
        this.classType = classType;
    }

    public Classes(int classID, int moduleID, int semesterID, int dow, String startTime, String endTime, String room, String classType) {
        this.classID = classID;
        this.moduleID = moduleID;
        this.semesterID = semesterID;
        this.dow = dow;
        this.startTime = startTime;
        this.endTime = endTime;
        this.room = room;
        this.classType = classType;
    }

    public int getClassID() {
        return classID;
    }

    public int getModuleID() {
        return moduleID;
    }

    public int getSemesterID() {
        return semesterID;
    }

    public int getDow() {
        return dow;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getRoom() {
        return room;
    }

    public String getClassType() {
        return classType;
    }

    @Override
    public String toString() {
        return "Classes{" +
                "classID=" + classID +
                ", moduleID=" + moduleID +
                ", semesterID=" + semesterID +
                ", dow=" + dow +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", room='" + room + '\'' +
                ", classType='" + classType + '\'' +
                '}';
    }
}
