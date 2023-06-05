package com.studentplanner.studentplanner.models;

import android.widget.AutoCompleteTextView;

import com.google.android.material.textfield.TextInputLayout;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Coursework {
    private int courseworkID;
    private int moduleID;
    private String title;
    private String description;
    private String priority;
    private String deadline;
    private String deadlineTime;
    private boolean isCompleted = false;
    private TextInputLayout txtTitle;
    private TextInputLayout txtModuleID;
    private TextInputLayout txtPriority;

    private AutoCompleteTextView txtDeadline;
    private AutoCompleteTextView txtDeadlineTime;
    private TextInputLayout txtDeadlineTimeError;

//    private static final Predicate<Coursework> filterTitle = c -> c.getTitle().toLowerCase().contains(title.toLowerCase());


    public TextInputLayout getTxtDeadlineError() {
        return txtDeadlineError;
    }

    public void setTxtDeadlineError(TextInputLayout txtDeadlineError) {
        this.txtDeadlineError = txtDeadlineError;
    }

    private TextInputLayout txtDeadlineError;


    public AutoCompleteTextView getTxtDeadline() {
        return txtDeadline;
    }

    public void setTxtDeadline(AutoCompleteTextView txtDeadline) {
        this.txtDeadline = txtDeadline;
    }

    public AutoCompleteTextView getTxtDeadlineTime() {
        return txtDeadlineTime;
    }

    public void setTxtDeadlineTime(AutoCompleteTextView txtDeadlineTime) {
        this.txtDeadlineTime = txtDeadlineTime;
    }

    public TextInputLayout getTxtDeadlineTimeError() {
        return txtDeadlineTimeError;
    }

    public void setTxtDeadlineTimeError(TextInputLayout txtDeadlineTimeError) {
        this.txtDeadlineTimeError = txtDeadlineTimeError;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }


    public TextInputLayout getTxtPriority() {
        return txtPriority;
    }


    public TextInputLayout getTxtModuleID() {
        return txtModuleID;
    }

    public TextInputLayout getTxtTitle() {
        return txtTitle;
    }


    public void setCourseworkID(int courseworkID) {
        this.courseworkID = courseworkID;
    }

    public void setModuleID(int moduleID) {
        this.moduleID = moduleID;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public void setDeadlineTime(String deadlineTime) {
        this.deadlineTime = deadlineTime;
    }

    public void setTxtTitle(TextInputLayout txtTitle) {
        this.txtTitle = txtTitle;
    }

    public void setTxtModuleID(TextInputLayout txtModuleID) {
        this.txtModuleID = txtModuleID;
    }

    public void setTxtPriority(TextInputLayout txtPriority) {
        this.txtPriority = txtPriority;
    }

    public Coursework() {

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

    public static Predicate<Coursework> filterTitle(String title) {
        return c -> c.getTitle().toLowerCase().contains(title.toLowerCase());

    }

    public static Predicate<Coursework> filterPriority(String priority) {
        return c -> c.getPriority().toLowerCase().contains(priority.toLowerCase());

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
