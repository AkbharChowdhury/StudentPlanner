package com.studentplanner.studentplanner.models;

import android.content.Context;

import com.studentplanner.studentplanner.R;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class SearchCoursework {

    private final List<Coursework> ALL_COURSEWORK;
    private final Context context;

    private String title = "";
    private String priority = "";
    private boolean isCompleted = false;


    public boolean isDefaultStatus() {
        return isDefaultStatus;
    }

    public void setDefaultStatus(boolean defaultStatus) {
        isDefaultStatus = defaultStatus;
    }

    private boolean isDefaultStatus = false;


    private final Predicate<Coursework> filterTitle = c -> c.getTitle().toLowerCase().contains(title.toLowerCase());
    private final Predicate<Coursework> filterPriority = c -> c.getPriority().toLowerCase().contains(priority.toLowerCase());
    private final Predicate<Coursework> filterCompletionStatus = c -> c.isCompleted() == isCompleted;


    public SearchCoursework(Context context, List<Coursework> ALL_COURSEWORK) {
        this.context = context;
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


    public List<Coursework> filterResults() {
        // any priority
        if (context.getResources().getString(R.string.any_priority).equalsIgnoreCase(priority)){
            return ALL_COURSEWORK.stream()
                    .filter(filterTitle)
                    .filter(filterCompletionStatus)
                    .collect(Collectors.toList());

        }


//        if (resetCompletionStatus){
//            return ALL_COURSEWORK.stream()
//                    .filter(filterTitle)
//                    .filter(filterPriority)
//                    .collect(Collectors.toList());
//
//        }
//
//
        // filter all category
        return ALL_COURSEWORK.stream()
                .filter(filterTitle)
                .filter(filterPriority)
                .filter(filterCompletionStatus)
                .collect(Collectors.toList());
    }

}
