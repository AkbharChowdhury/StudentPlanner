package com.studentplanner.studentplanner.addActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.view.*;
import android.widget.*;

import com.google.android.material.textfield.TextInputLayout;
import com.studentplanner.studentplanner.DatabaseHelper;
import com.studentplanner.studentplanner.R;
import com.studentplanner.studentplanner.enums.DatePickerType;
import com.studentplanner.studentplanner.fragments.ReminderFragment;
import com.studentplanner.studentplanner.models.Semester;
import com.studentplanner.studentplanner.utils.CalendarUtils;
import com.studentplanner.studentplanner.utils.DatePickerFragment;
import com.studentplanner.studentplanner.utils.Helper;
import com.studentplanner.studentplanner.utils.Validation;

import java.time.LocalDate;

public class AddSemesterActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private AutoCompleteTextView txtStartDate;
    private AutoCompleteTextView txtEndDate;
    private DatePickerType type;
    private DatePickerFragment datePickerStart;
    private DatePickerFragment datePickerEnd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_semester);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        findTextFields();
        setUpDatePickers();
        setDefaultValues();

        findViewById(R.id.btn_add_semester).setOnClickListener(v -> {
            DatabaseHelper db = DatabaseHelper.getInstance(this);
            Validation form = new Validation(this);
            TextInputLayout txtSemesterName = findViewById(R.id.txtSemester);

            String name = txtSemesterName.getEditText().getText().toString().trim();
            LocalDate start = LocalDate.parse(Helper.convertFUllDateToYYMMDD(txtStartDate.getEditableText().toString()));
            LocalDate end = LocalDate.parse(Helper.convertFUllDateToYYMMDD(txtEndDate.getEditableText().toString()));

            if (form.validateSemesterForm(txtSemesterName)){
                if (db.addSemester(new Semester(name, start, end))){
                    Helper.setRedirectMessageFragment(this, ReminderFragment.class, "Semester Added");

                }

            }
        });


    }

    private void setUpDatePickers() {
        setStartDatePicker();
        setEndDatePicker();

    }

    private void setDefaultValues() {
        txtStartDate.setText(Helper.formatDate(CalendarUtils.getCurrentDate().toString()));
        txtEndDate.setText(Helper.formatDate(CalendarUtils.getCurrentDate().plusWeeks(3).toString()));
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setStartDatePicker() {
        txtStartDate.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                type = DatePickerType.START_DATE;
                datePickerStart = new DatePickerFragment();
                datePickerStart.show(getSupportFragmentManager(), "datePickerStart");
                datePickerStart.setMinDateToToday();
                datePickerStart.setConstrainEndDate();
                createDatePickerConstraint(datePickerStart);
                CalendarUtils.setSelectedDate(datePickerStart, txtStartDate);


            }
            return false;
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setEndDatePicker() {
        txtEndDate.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                type = DatePickerType.END_DATE;
                datePickerEnd = new DatePickerFragment();
                datePickerEnd.show(getSupportFragmentManager(), "datePickerEnd");
                datePickerEnd.setConstrainStartDate();
                createDatePickerConstraint(datePickerEnd);
                CalendarUtils.setSelectedDate(datePickerEnd, txtEndDate);


            }
            return false;
        });
    }



    private void findTextFields() {
        txtStartDate = findViewById(R.id.txtStartDate);
        txtEndDate = findViewById(R.id.txtEndDate);
    }

    private void createDatePickerConstraint(DatePickerFragment datePickerStart) {
        String endDate = Helper.convertFUllDateToYYMMDD(txtEndDate.getEditableText().toString());
        String startDate = Helper.convertFUllDateToYYMMDD(txtStartDate.getEditableText().toString());
        datePickerStart.setDatePickerStartEnd(LocalDate.parse(startDate), LocalDate.parse(endDate));
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onDateSet(DatePicker d, int year, int month, int day) {

        LocalDate date = Helper.formatDate(year, month, day);
        String YY_MM_DD_FORMAT = String.valueOf(date);
        String formattedDate = Helper.formatDate(YY_MM_DD_FORMAT);
        switch (type) {
            case START_DATE:
                txtStartDate.setText(formattedDate);
                break;
            case END_DATE:
                txtEndDate.setText(formattedDate);
                break;
        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) finish();
        return true;
    }




}