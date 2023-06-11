package com.studentplanner.studentplanner.models;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.studentplanner.studentplanner.DatabaseHelper;
import com.studentplanner.studentplanner.R;
import com.studentplanner.studentplanner.databinding.EventRowBinding;
import com.studentplanner.studentplanner.utils.Helper;

import java.util.List;

public class ClassRow {
    private final Context context;

    private final TextView lblTeachers;
    private final TextView lblClassTitle;
    private final TextView lblRoom;
    private final TextView lblStartTime;
    private final TextView lblEndTime;
    private final TextView lblType;
    private final DatabaseHelper db;

    public ClassRow(EventRowBinding binding, Context context) {
        this.context = context;
        this.db = DatabaseHelper.getInstance(context);
        lblTeachers = binding.tvClassTeachers;
        lblClassTitle =  binding.tvClassTitle;
        lblRoom = binding.tvRoom;
        lblStartTime = binding.tvStartTime;
        lblEndTime = binding.tvEndTime;
        lblType = binding.tvClassType;
    }
    public void setDetails(final Classes classes){
        final int moduleID = classes.getModuleID();

        List<ModuleTeacher> moduleTeacherList = db.getModuleTeachers();

        lblType.setText(classes.getClassType());
        String teachers = Helper.moduleIDExistsInModuleTeacher(moduleTeacherList, classes.getModuleID())
                ? Helper.getSnippet(Helper.getTeachersForSelectedModule(context, moduleID), 35)
                : context.getString(R.string.no_teacher_assigned);

        lblTeachers.setText(teachers);
        Module module = db.getSelectedModule(moduleID);
        lblClassTitle.setText(module.getModuleName());
        String room = classes.getRoom().isEmpty() ? context.getString(R.string.no_room_assigned) : classes.getRoom();
        lblRoom.setText(room);
        lblStartTime.setText(Helper.formatTimeShort(classes.getStartTime()));
        lblEndTime.setText(Helper.formatTimeShort(classes.getEndTime()));
    }
}
