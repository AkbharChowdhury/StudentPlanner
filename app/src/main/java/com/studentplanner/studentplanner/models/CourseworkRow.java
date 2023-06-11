package com.studentplanner.studentplanner.models;

import android.view.View;
import android.widget.TextView;

import com.studentplanner.studentplanner.R;

public class CourseworkRow {
    public TextView title;
    public TextView lblModule;
    public TextView priority;
    public TextView time;
    public TextView completionStatus;

    public CourseworkRow(View convertView){
        title = convertView.findViewById(R.id.tv_cw_title);
        lblModule = convertView.findViewById(R.id.tv_cw_module);
        priority = convertView.findViewById(R.id.tv_cw_priority);
        time = convertView.findViewById(R.id.tv_cw_time);
        completionStatus = convertView.findViewById(R.id.tv_cw_completed);

    }
}