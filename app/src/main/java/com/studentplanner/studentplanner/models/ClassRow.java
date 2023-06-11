package com.studentplanner.studentplanner.models;

import android.view.View;
import android.widget.TextView;

import com.studentplanner.studentplanner.R;

public class ClassRow {
    public TextView lblTeachers;
    public TextView lblClassTitle;
    public TextView lblRoom;
    public TextView lblStartTime;
    public TextView lblEndTime;
    public TextView lblType;

    public ClassRow(View convertView) {
        lblTeachers = convertView.findViewById(R.id.tv_class_teachers);
        lblClassTitle = convertView.findViewById(R.id.tv_class_title);
        lblRoom = convertView.findViewById(R.id.tv_room);
        lblStartTime = convertView.findViewById(R.id.tv_start_time);
        lblEndTime = convertView.findViewById(R.id.tv_end_time);
        lblType = convertView.findViewById(R.id.tv_class_type);
    }
}
