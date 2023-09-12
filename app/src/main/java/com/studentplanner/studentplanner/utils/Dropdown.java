package com.studentplanner.studentplanner.utils;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;

import com.studentplanner.studentplanner.R;

import java.util.Arrays;
import java.util.List;

public final class Dropdown {
    private Dropdown() {}

    public static void getStringArray(AutoCompleteTextView field, Context c, int array) {
        field.setAdapter(new ArrayAdapter<>(c, R.layout.list_item, c.getResources().getStringArray(array)));
    }


    public static int getSelectedStringArrayNumber(String priority, Context c, int array) {
        return Arrays.asList(c.getResources().getStringArray(array)).indexOf(priority);
    }


    public static int setSelectedDay(String day) {
        return CalendarUtils.getDays().indexOf(day);

    }

    public static int getDropDownID(final int id, final List<Integer> list) {
        return list.indexOf(id);

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
