package com.studentplanner.studentplanner.utils;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.studentplanner.studentplanner.R;
import com.studentplanner.studentplanner.models.Module;
import com.studentplanner.studentplanner.models.Semester;

import java.util.List;
import java.util.stream.IntStream;

public final class Dropdown {
    private Dropdown(){

    }
    public static void getStringArray(AutoCompleteTextView field, Context context, int array){
        field.setAdapter(new ArrayAdapter<>(context, R.layout.list_item, context.getResources().getStringArray(array)));


    }


    public static int getSelectedStringArrayNumber(String priority, Context context, int array) {
        String[] items = context.getResources().getStringArray(array);
        int num = 0;
        for (int i = 0; i < items.length; i++) {
            if (items[i].equalsIgnoreCase(priority)) {
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

        for (int i = 0; i < days.size(); i++) {
            if (days.get(i).equalsIgnoreCase(day)) {
                return i;
            }
        }
        return 0;




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
