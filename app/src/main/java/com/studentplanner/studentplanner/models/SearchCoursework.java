package com.studentplanner.studentplanner.models;

import android.content.Context;

import com.studentplanner.studentplanner.R;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class SearchCoursework {

    private final List<Coursework> ALL_COURSEWORK;

    private String title = "";
    private String priority = "";
    private boolean isCompleted = false;

    public void setDefaultStatus(boolean defaultStatus) {
        isDefaultStatus = defaultStatus;
    }


    /**
     * specifies if the default completion status is selected (Any Completion).
     */
    private boolean isDefaultStatus = true;

    private final Predicate<Coursework> filterTitle = c -> c.getTitle().toLowerCase().contains(title.toLowerCase());

    private final Predicate<Coursework> filterPriority;


    public SearchCoursework(Context context, List<Coursework> ALL_COURSEWORK) {
        this.ALL_COURSEWORK = ALL_COURSEWORK;
        filterPriority = !priority.equals(context.getResources().getString(R.string.any_priority)) ? c -> c.getPriority().toLowerCase().contains(priority.toLowerCase()) : p -> true;

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }


    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
    private  boolean filterCompletionStatus(Coursework c){
        return isDefaultStatus || c.isCompleted() == isCompleted;
    }


    public List<Coursework> filterResults() {
        return ALL_COURSEWORK.stream()
                .filter(filterTitle)
                .filter(filterPriority)
                .filter(this::filterCompletionStatus)
                .collect(Collectors.toList());
    }


}
