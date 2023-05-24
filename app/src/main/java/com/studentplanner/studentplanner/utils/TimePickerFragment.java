package com.studentplanner.studentplanner.utils;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.HOURS;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.studentplanner.studentplanner.R;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class TimePickerFragment extends DialogFragment {

    private TimePickerDialog timePicker;
    

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        createTimePicker();
        return timePicker;



    }

    private void createTimePicker() {
        int hour = LocalDateTime.now().plusHours(1).getHour();
        int minute = LocalDateTime.now().getMinute();
        timePicker = new TimePickerDialog(getActivity(), AlertDialog.THEME_HOLO_DARK, (TimePickerDialog.OnTimeSetListener) getActivity(), hour, minute, false);
        timePicker.setTitle(getString(R.string.timeLabel));
        timePicker.show();
    }

}
