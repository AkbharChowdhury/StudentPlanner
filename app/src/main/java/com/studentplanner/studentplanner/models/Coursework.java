package com.studentplanner.studentplanner.models;

import com.google.android.material.textfield.TextInputLayout;

public class Coursework {
    private int courseworkID;
    private int moduleID;
    private String title;
    private String description;
    private String priority;
    private String deadline;
    private String deadlineTime;
    private boolean isCompleted = false;

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    private TextInputLayout txtTitle;
    private TextInputLayout txtModuleID;

    public TextInputLayout getTxtPriority() {
        return txtPriority;
    }

    private TextInputLayout txtPriority;


    public TextInputLayout getTxtModuleID() {
        return txtModuleID;
    }

    public TextInputLayout getTxtTitle() {
        return txtTitle;
    }

    public Coursework(TextInputLayout txtTitle, TextInputLayout txtModuleID, TextInputLayout txtPriority) {
        this.txtTitle = txtTitle;
        this.txtModuleID = txtModuleID;
        this.txtPriority = txtPriority;
    }

    public Coursework(int moduleID, String title, String description, String priority, String deadline, String deadlineTime) {
        this.moduleID = moduleID;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.deadline = deadline;
        this.deadlineTime = deadlineTime;
    }

    public Coursework(int courseworkID, int moduleID, String title, String description, String priority, String deadline, String deadlineTime) {
        this.courseworkID = courseworkID;
        this.moduleID = moduleID;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.deadline = deadline;
        this.deadlineTime = deadlineTime;

    }

    public int getCourseworkID() {
        return courseworkID;
    }

    public int getModuleID() {
        return moduleID;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getPriority() {
        return priority;
    }

    public String getDeadline() {
        return deadline;
    }

    public String getDeadlineTime() {
        return deadlineTime;
    }

    @Override
    public String toString() {
        return "Coursework{" +
                "courseworkID=" + courseworkID +
                ", moduleID=" + moduleID +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", priority='" + priority + '\'' +
                ", deadline='" + deadline + '\'' +
                ", deadlineTime='" + deadlineTime + '\'' +
                '}';
    }
}
