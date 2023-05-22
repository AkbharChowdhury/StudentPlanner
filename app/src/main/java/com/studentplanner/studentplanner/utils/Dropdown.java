package com.studentplanner.studentplanner.utils;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.google.android.material.textfield.TextInputLayout;
import com.studentplanner.studentplanner.R;
import com.studentplanner.studentplanner.models.Module;
import com.studentplanner.studentplanner.models.Semester;
import com.studentplanner.studentplanner.utils.CalendarUtils;
import com.studentplanner.studentplanner.utils.Helper;

import java.util.List;

public final class Dropdown {
    private Dropdown(){

    }
    public static void getStringArray(AutoCompleteTextView field, Context context, int array){
        field.setAdapter(new ArrayAdapter<>(context, R.layout.list_item, context.getResources().getStringArray(array)));


    }
    public static int getPriorityNumber(String priority, Context context) {
        String[] items = context.getResources().getStringArray(R.array.priority_array);
        int num = 0;
        for (int i = 0; i < items.length; i++) {
            if (items[i].equalsIgnoreCase(priority)) {
                num = i;
            }
        }
        return num;


    }

    public static int getClassTypeNumber(String type, Context context) {
        String[] items = context.getResources().getStringArray(R.array.type_array);
        int num = 0;
        for (int i = 0; i < items.length; i++) {
            if (items[i].equalsIgnoreCase(type)) {
                num = i;
            }
        }
        return num;


    }
    public static int getModuleID(int moduleID, List<Module> moduleList) {
        int size = moduleList.size();
        int num = 0;
        for (int i = 0; i < size; i++) {
            if (moduleID == moduleList.get(i).getModuleID()) {
                num = i;
            }
        }
        return num;
    }


    public static int setSelectedDay(String day) {
        List<String> days = CalendarUtils.getDays();
        int num = 0;
        for (int i = 0; i < days.size(); i++) {
            if (days.get(i).equalsIgnoreCase(day)) {
                num = i;
            }
        }
        return num;
    }

    public static int getSemesterID(int semesterID, List<Semester> semesterList) {
        int size = semesterList.size();
        int num = 0;
        for (int i = 0; i < size; i++) {
            if (semesterID == semesterList.get(i).getSemesterID()) {
                num = i;
            }
        }
        return num;
    }


}
