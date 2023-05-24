package com.studentplanner.studentplanner.utils;


import static java.time.temporal.ChronoUnit.DAYS;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.app.Dialog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.time.LocalDate;

public class DatePickerFragment extends DialogFragment {
    private boolean setMinDateToNow = false;
    private boolean isConstrained = false;
    private boolean isConstrainStartDate = false;
    private boolean isConstrainEndDate = false;
    private boolean isCustomDate = false;
    private LocalDate selectedDate;
    private DatePickerDialog datePicker;

    private LocalDate startDate;
    private LocalDate endDate;

    public void setCustomDate(LocalDate selectedDate) {
        isCustomDate = true;
        this.selectedDate = selectedDate;
    }

    public void setConstrainStartDate() {
        isConstrainStartDate = true;
    }

    public void setConstrainEndDate() {
        isConstrainEndDate = true;
    }


    public void setDatePickerStartEnd(LocalDate start, LocalDate end) {
        isConstrained = true;
        startDate = start;
        endDate = end;
    }





    public void setMinDateToToday() {
        setMinDateToNow = true;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        createDatePicker();
        if (setMinDateToNow) {
            datePicker.getDatePicker().setMinDate(Helper.getNow());
        }

        setConstraints();
        return datePicker;
    }

    private void setConstraints() {
        if (isConstrained) {
            long startDateLong = Helper.convertLocalDateToLong(startDate);
            long noOfDaysBetween = DAYS.between(startDate, endDate);

            if (isConstrainStartDate) {
                datePicker.getDatePicker().setMinDate(startDateLong);
            }
            if (isConstrainEndDate) {
                datePicker.getDatePicker().setMaxDate(Helper.setFutureDate(startDateLong, noOfDaysBetween));
            }
        }
    }

    private void createDatePicker() {
        int year;
        int month;
        int day;
        if (isCustomDate) {
            // show custom date when the datePicker is shown
            year = selectedDate.getYear();
            month = selectedDate.getMonthValue();
            day = selectedDate.getDayOfMonth();
        } else {
            // show current date
            LocalDate d = LocalDate.now();
            year = d.getYear();
            month = d.getMonthValue();
            day = d.getDayOfMonth();
        }
        datePicker = new DatePickerDialog(getActivity(), (DatePickerDialog.OnDateSetListener) getActivity(), year, --month, day);
    }


}
