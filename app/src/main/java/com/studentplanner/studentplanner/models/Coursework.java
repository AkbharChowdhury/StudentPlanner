package com.studentplanner.studentplanner.models;

import android.widget.AutoCompleteTextView;

import com.google.android.material.textfield.TextInputLayout;
import com.studentplanner.studentplanner.interfaces.Searchable;

import java.time.LocalDate;
import java.util.Comparator;

public class Coursework implements Searchable {
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

    private byte image;

    public byte getImage() {
        return image;
    }

    public void setImage(byte image) {
        this.image = image;
    }

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

    public static Comparator<Coursework> sortDeadlineAsc = Comparator.comparing(c -> LocalDate.parse(c.getDeadline()));
    public static Comparator<Coursework> sortDeadlineDesc = (c1, c2) -> c2.getDeadline().compareTo(LocalDate.now().toString());


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

    @Override
    public String searchText() {
        return title;
    }
}
