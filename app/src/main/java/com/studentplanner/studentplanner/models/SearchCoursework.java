package com.studentplanner.studentplanner.models;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class SearchCoursework {

    private final List<Coursework> ALL_COURSEWORK;

    private String title = "";
    private String priority = "";
    private boolean isCompleted = false;
    private boolean resetCompletionStatus = false;


    private final Predicate<Coursework> filterTitle = c -> c.getTitle().toLowerCase().contains(title.toLowerCase());
    private final Predicate<Coursework> filterPriority = c -> c.getPriority().toLowerCase().contains(priority.toLowerCase());
    private final Predicate<Coursework> filterCompletionStatus = c -> c.isCompleted() == isCompleted;


    public SearchCoursework(List<Coursework> ALL_COURSEWORK) {
        this.ALL_COURSEWORK = ALL_COURSEWORK;
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

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
    public void resetCompleted(){
        resetCompletionStatus = true;

    }

    public List<Coursework> filterResults() {


//        if (resetCompletionStatus){
//            return ALL_COURSEWORK.stream()
//                    .filter(filterTitle)
//                    .filter(filterPriority)
//                    .collect(Collectors.toList());
//
//        }
//
//
        return ALL_COURSEWORK.stream()
                .filter(filterTitle)
                .filter(filterPriority)
                .filter(filterCompletionStatus)
                .collect(Collectors.toList());
    }

}
