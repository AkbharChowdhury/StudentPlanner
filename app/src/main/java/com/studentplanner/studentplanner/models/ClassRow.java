package com.studentplanner.studentplanner.models;

import android.view.View;
import android.widget.TextView;

import com.studentplanner.studentplanner.R;
import com.studentplanner.studentplanner.databinding.EventRowBinding;

public class ClassRow {
    public TextView lblTeachers;
    public TextView lblClassTitle;
    public TextView lblRoom;
    public TextView lblStartTime;
    public TextView lblEndTime;
    public TextView lblType;

    public ClassRow(EventRowBinding binding) {
        lblTeachers = binding.tvClassTeachers;
        lblClassTitle =  binding.tvClassTitle;
        lblRoom = binding.tvRoom;
        lblStartTime = binding.tvStartTime;
        lblEndTime = binding.tvEndTime;
        lblType =binding.tvClassType;
    }
}
