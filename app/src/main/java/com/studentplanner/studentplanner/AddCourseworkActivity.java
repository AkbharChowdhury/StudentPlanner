package com.studentplanner.studentplanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;

import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.google.android.material.textfield.TextInputLayout;
import com.studentplanner.studentplanner.models.Coursework;
import com.studentplanner.studentplanner.models.Module;
import com.studentplanner.studentplanner.tables.CourseworkTable;
import com.studentplanner.studentplanner.tables.ModuleTable;
import com.studentplanner.studentplanner.utils.CalendarUtils;
import com.studentplanner.studentplanner.utils.DatePickerFragment;
import com.studentplanner.studentplanner.utils.Dropdown;
import com.studentplanner.studentplanner.utils.Helper;
import com.studentplanner.studentplanner.utils.TimePickerFragment;
import com.studentplanner.studentplanner.utils.Validation;

import java.time.LocalDate;

import java.time.LocalTime;
import java.util.List;
import java.util.Locale;

public class AddCourseworkActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private AutoCompleteTextView txtPriority;
    private AutoCompleteTextView txtModules;

    private AutoCompleteTextView txtDeadline;
    private AutoCompleteTextView txtDeadlineTime;
    private TextInputLayout txtTitle;
    private TextInputLayout txtDescription;
    private int selectedModuleID;
    final String SELECT_PRIORITY = "Select Priority";
    private DatabaseHelper db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_coursework);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        db = DatabaseHelper.getInstance(this);

        txtPriority = findViewById(R.id.txtPriority);
        txtDeadline = findViewById(R.id.txtDeadline);
        txtDeadlineTime = findViewById(R.id.txtDeadlineTime);
        txtModules = findViewById(R.id.txtModule);
        txtModules.setText(R.string.select_module);
        txtPriority.setText(SELECT_PRIORITY);
        txtTitle = findViewById(R.id.txtTitle);
        txtDescription = findViewById(R.id.txtDescription);
        setTimePicker();


        txtDeadlineTime.setText(Helper.showFormattedDBTime(LocalTime.now().plusHours(1).toString(), this));

        Dropdown.getStringArray(txtPriority, this, R.array.priority_array);
        txtDeadline.setText(Helper.formatDate(CalendarUtils.getCurrentDate().toString()));

        if (getIntent().getStringExtra(CourseworkTable.COLUMN_DEADLINE) != null) {
            txtDeadline.setText(getIntent().getStringExtra(CourseworkTable.COLUMN_DEADLINE));

        }
        setupDatePicker();
        getModulesList();


        findViewById(R.id.btn_add_coursework).setOnClickListener(v -> {
            Validation form = new Validation(this);
            TextInputLayout txtModuleError = findViewById(R.id.txtModuleError);
            TextInputLayout errorPriority = findViewById(R.id.txtPriorityError);
            if (form.validateAddCourseworkForm(new Coursework(txtTitle, txtModuleError, errorPriority))) {
                if (db.addCoursework(getCourseworkDetails())) {
                    Helper.longToastMessage(this, "Coursework Added");
                    Helper.setRedirectMessageFragment(this, CourseworkFragment.class, "Coursework Added");
                }

            }


        });


    }

    private Coursework getCourseworkDetails() {

        return new Coursework(

                selectedModuleID,
                Helper.trimStr(txtTitle),
                Helper.trimStr(txtDescription),
                Helper.trimStr(txtPriority),
                Helper.convertFUllDateToYYMMDD(Helper.trimStr(txtDeadline)),
                Helper.convertFormattedTimeToDBFormat(txtDeadlineTime.getText().toString())

        );
    }

    private void getModulesList() {

        final List<Module> moduleList = db.getModules();
        final List<String> items = Module.populateDropdown(moduleList);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_item, items);
        txtModules.setAdapter(adapter);
        txtModules.setOnItemClickListener((parent, view, position, id) -> selectedModuleID = moduleList.get(position).getModuleID());
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onDateSet(DatePicker d, int year, int month, int day) {
        LocalDate date = Helper.formatDate(year, month, day);
        txtDeadline.setText(Helper.formatDate(String.valueOf(date)));
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setupDatePicker() {
        txtDeadline.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                DatePickerFragment datepicker = new DatePickerFragment();
                datepicker.show(getSupportFragmentManager(), "datePicker");
                datepicker.setMinDateToToday();
                CalendarUtils.setSelectedDate(datepicker, txtDeadline);
            }
            return false;
        });
    }


    @SuppressLint("ClickableViewAccessibility")
    private void setTimePicker() {
        txtDeadlineTime.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                new TimePickerFragment().show(getSupportFragmentManager(), "timePicker");
            }

            return false;
        });
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
        String selectedTime = String.format(Locale.getDefault(), getString(R.string.time_format_database), selectedHour, selectedMinute);
        txtDeadlineTime.setText(Helper.formatTime(selectedTime));

    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) finish();
        return true;
    }

}