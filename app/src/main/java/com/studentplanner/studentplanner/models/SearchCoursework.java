package com.studentplanner.studentplanner.models;

import static com.studentplanner.studentplanner.models.Coursework.sortDeadlineAsc;
import static com.studentplanner.studentplanner.models.Coursework.sortDeadlineDesc;

import android.content.Context;

import com.studentplanner.studentplanner.R;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class SearchCoursework {


    private final List<Coursework> ALL_COURSEWORK;

    private String title = "";
    private String priority = "";
    private boolean isCompleted = false;
    private final String DEFAULT_PRIORITY;

    /**
     * specifies if the default completion status is selected (Any Completion).
     */
    private boolean isDefaultStatus = true;

    private final Predicate<Coursework> filterTitle = c -> c.getTitle().toLowerCase().contains(title.toLowerCase());


    public SearchCoursework(Context context, List<Coursework> ALL_COURSEWORK) {

        this.ALL_COURSEWORK = ALL_COURSEWORK;

        DEFAULT_PRIORITY = context.getResources().getString(R.string.any_priority);

    }

    private Predicate<Coursework> filterPriority() {
        return !priority.equals(DEFAULT_PRIORITY) ? c -> c.getPriority().toLowerCase().contains(priority.toLowerCase()) : p -> true;

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

    public void setDefaultStatus(boolean defaultStatus) {
        isDefaultStatus = defaultStatus;
    }


    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    private boolean filterStatus(Coursework c) {
        return isDefaultStatus || c.isCompleted() == isCompleted;
    }


    public List<Coursework> filterResults(boolean isLatest) {

        return ALL_COURSEWORK.stream()
                .filter(filterTitle)
                .filter(filterPriority())
                .filter(this::filterStatus).sorted(isLatest?  sortDeadlineDesc: sortDeadlineAsc).collect(Collectors.toList());

//        return ALL_COURSEWORK.stream()
//                .filter(filterTitle)
//                .filter(filterPriority())
//                .filter(this::filterStatus)
//                .collect(Collectors.toList());
    }


}
