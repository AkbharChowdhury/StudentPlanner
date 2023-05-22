package com.studentplanner.studentplanner;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TimePicker;

import com.google.android.material.textfield.TextInputLayout;
import com.studentplanner.studentplanner.enums.TimePickerType;
import com.studentplanner.studentplanner.models.Classes;
import com.studentplanner.studentplanner.models.Module;
import com.studentplanner.studentplanner.models.Semester;
import com.studentplanner.studentplanner.utils.CalendarUtils;
import com.studentplanner.studentplanner.utils.Helper;
import com.studentplanner.studentplanner.utils.TimePickerFragment;
import com.studentplanner.studentplanner.utils.Validation;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class AddClassesActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {
    private AutoCompleteTextView txtDays;
    private AutoCompleteTextView txtModules;
    private AutoCompleteTextView txtSemester;
    private AutoCompleteTextView txtClassType;

    private AutoCompleteTextView txtStartTime;
    private AutoCompleteTextView txtEndTime;
    private TimePickerFragment timePickerStart;
    private TimePickerFragment timePickerEnd;

    private int selectedModuleID;
    private int selectedSemesterID;
    private TimePickerType type;
    private TextInputLayout txtRoom;


    private DatabaseHelper db;
    private Validation form;


    TextInputLayout txtDayError;
    TextInputLayout txtSemesterError;
    TextInputLayout txtModuleError;
    TextInputLayout txtClassTypeError;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_classes);

        txtDayError = findViewById(R.id.txtDayError);
        txtSemesterError = findViewById(R.id.txtSemesterErrorClasses);
        txtModuleError = findViewById(R.id.txtModuleErrorClasses);
        txtClassTypeError = findViewById(R.id.txtClassTypeError);
        form = new Validation(this);


        txtDays = findViewById(R.id.txtDay);
        txtModules = findViewById(R.id.txtModuleClasses);
        txtSemester = findViewById(R.id.txtSemesterClasses);
        txtClassType = findViewById(R.id.txtClassType);
        txtStartTime = findViewById(R.id.txtStartTime);
        txtEndTime = findViewById(R.id.txtEndTime);
        txtRoom = findViewById(R.id.txtRoom);

        txtDays.setText(getString(R.string.select_day));
        txtSemester.setText(getString(R.string.select_semester));
        txtModules.setText(getString(R.string.select_module));
        txtClassType.setText(getString(R.string.select_class_type));
        txtStartTime.setText(Helper.showFormattedDBTime(LocalTime.now().plusHours(1).toString(), this));
        txtEndTime.setText(Helper.showFormattedDBTime(LocalTime.now().plusHours(2).toString(), this));


        db = DatabaseHelper.getInstance(this);
        setUpTimePickers();
        Helper.getDays(txtDays, this);
        getModulesList();
        getSemesterList();
        Helper.getStringArray(this, txtClassType, R.array.type_array);

        findViewById(R.id.btn_add_classes).setOnClickListener(v -> {

            Classes errorFields = new Classes(txtDayError, txtSemesterError, txtModuleError, txtClassTypeError);
            if (form.validateClassForm(errorFields)){
                if (db.addClass(getClassDetails())){
                    Helper.setRedirectMessageFragment(this, ReminderFragment.class, "Class Added");

                }
            }


        });
    }

    private Classes getClassDetails() {
        return new Classes(
                selectedModuleID,
                selectedSemesterID,
                CalendarUtils.getDOWNumber(Helper.trimStr(txtDays)),
                Helper.convertFormattedTimeToDBFormat(Helper.trimStr(txtStartTime)),
                Helper.convertFormattedTimeToDBFormat(Helper.trimStr(txtEndTime)),
                Helper.trimStr(txtRoom),
                Helper.trimStr(txtClassType)

        );
    }


    private void getModulesList() {

        final List<Module> moduleList = db.getModules();
        final List<String> items = Module.populateDropdown(moduleList);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_item, items);

        txtModules.setAdapter(adapter);
        txtModules.setOnItemClickListener((parent, view, position, id) -> selectedModuleID = moduleList.get(position).getModuleID());

    }


    private void getSemesterList() {

        final List<Semester> list = db.getSemester();
        final List<String> items = Semester.populateDropdown(list);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_item, items);
        txtSemester.setAdapter(adapter);
        txtSemester.setOnItemClickListener((parent, view, position, id) -> selectedSemesterID = list.get(position).getSemesterID());

    }

    @Override
    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
        String selectedTime = String.format(Locale.getDefault(), getString(R.string.time_format_database), selectedHour, selectedMinute);
        String formattedTime = Helper.formatTime(selectedTime);
        switch (type) {
            case START_TIME:
                txtStartTime.setText(formattedTime);
                break;
            case END_TIME:
                txtEndTime.setText(formattedTime);
                break;
        }

    }

    private void setUpTimePickers() {
        setStartTimePicker();
        setEndTimePicker();


    }

    @SuppressLint("ClickableViewAccessibility")
    private void setStartTimePicker() {
        txtStartTime.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                type = TimePickerType.START_TIME;
                timePickerStart = new TimePickerFragment();
                timePickerStart.show(getSupportFragmentManager(), "timePickerStart");

            }

            return false;
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setEndTimePicker() {
        txtEndTime.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                type = TimePickerType.END_TIME;
                timePickerEnd = new TimePickerFragment();
                timePickerEnd.show(getSupportFragmentManager(), "timePickerEnd");

            }

            return false;
        });
    }
}