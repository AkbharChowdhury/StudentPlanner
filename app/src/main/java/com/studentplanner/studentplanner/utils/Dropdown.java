package com.studentplanner.studentplanner.utils;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;

import com.studentplanner.studentplanner.R;

import java.util.List;

public final class Dropdown {
    private Dropdown() {

    }

    public static void getStringArray(AutoCompleteTextView field, Context context, int array) {
        field.setAdapter(new ArrayAdapter<>(context, R.layout.list_item, context.getResources().getStringArray(array)));
    }


    public static int getSelectedStringArrayNumber(String priority, Context context, int array) {
        String[] items = context.getResources().getStringArray(array);
        for (int i = 0; i < items.length; i++) {
            if (items[i].equalsIgnoreCase(priority)) {
                return i;
            }
        }
        return 0;
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


    public static int getDropDownID(final int id, final List<Integer> list) {
        for (int i = 0; i < list.size(); i++) {
            if (id == list.get(i)) {
                return i;
            }
        }
        return 0;
    }

    public static void setDefaultSpinnerPosition(Spinner... spinners) {
        for (Spinner spinner : spinners) {
            spinner.setSelection(0, false);

        }

    }
    public static String getSpinnerText(Spinner spinner, int position) {
        return spinner.getAdapter().getItem(position).toString();

    }

}
