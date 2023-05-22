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

    private boolean isConstrainStartTime = false;
    private boolean isConstrainEndTime = false;
    private boolean isCustomTime = false;
    private LocalTime selectedTime;
    private boolean isConstrained = false;
    private LocalTime startTime;
    private LocalTime endTime;
    private TimePickerDialog timePicker;


    public void setCustomTime(LocalTime selectedTime) {
        isCustomTime = true;
        this.selectedTime = selectedTime;
    }

    public void setConstrainStartTime(boolean constrainStartTime) {
        isConstrainStartTime = constrainStartTime;
    }

    public void setTimePickerStartEnd(LocalTime start, LocalTime end) {
        isConstrained = true;
        startTime = start;
        endTime = end;
    }

    private void setConstraints() {
        if (isConstrained) {
            long startTimeLong = Helper.convertLocalTimeToLong(startTime);
            long noOfDaysBetween = HOURS.between(startTime, endTime);

            if (isConstrainStartTime) {
//                timePicker.setM
//            ().setMinDate(startTimeLong);
            }
            if (isConstrainEndTime) {
//                datePicker.getDatePicker().setMaxDate(Helper.setFutureDate(startTimeLong, noOfDaysBetween));
            }
        }
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        createTimePicker();
//        setConstraints();
        return timePicker;


//        int hour = LocalDateTime.now().getHour();
//        int minute = LocalDateTime.now().getMinute();
//        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), AlertDialog.THEME_HOLO_DARK ,(TimePickerDialog.OnTimeSetListener) getActivity(), hour, minute, false);
//        timePickerDialog.setTitle(getString(R.string.timeLabel));
//        timePickerDialog.show();
//        return timePickerDialog;

    }

    private void createTimePicker() {
        int hour = LocalDateTime.now().plusHours(1).getHour();
        int minute = LocalDateTime.now().getMinute();
        timePicker = new TimePickerDialog(getActivity(), AlertDialog.THEME_HOLO_DARK, (TimePickerDialog.OnTimeSetListener) getActivity(), hour, minute, false);
        timePicker.setTitle(getString(R.string.timeLabel));
        timePicker.show();
//        timePicker.getContext().max
    }

}
