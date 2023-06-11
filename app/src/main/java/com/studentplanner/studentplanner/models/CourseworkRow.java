package com.studentplanner.studentplanner.models;

import android.view.View;
import android.widget.TextView;

import com.studentplanner.studentplanner.R;
import com.studentplanner.studentplanner.databinding.EventRowBinding;

public class CourseworkRow {
    public TextView title;
    public TextView lblModule;
    public TextView priority;
    public TextView time;
    public TextView completionStatus;

    public CourseworkRow(EventRowBinding binding){
        title = binding.tvCwTitle;
        lblModule = binding.tvCwModule;
        priority = binding.tvCwPriority;
        time = binding.tvCwTime;
        completionStatus = binding.tvCwCompleted;

    }
}