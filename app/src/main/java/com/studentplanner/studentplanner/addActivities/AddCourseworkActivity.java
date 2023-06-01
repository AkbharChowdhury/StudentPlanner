package com.studentplanner.studentplanner.addActivities;

import static com.studentplanner.studentplanner.utils.Helper.deadlineSetup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;

import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.google.android.material.textfield.TextInputLayout;
import com.studentplanner.studentplanner.DatabaseHelper;
import com.studentplanner.studentplanner.R;
import com.studentplanner.studentplanner.databinding.ActivityAddCourseworkBinding;
import com.studentplanner.studentplanner.databinding.ActivityAddModuleBinding;
import com.studentplanner.studentplanner.models.Coursework;
import com.studentplanner.studentplanner.models.CustomTimePicker;
import com.studentplanner.studentplanner.models.Module;
import com.studentplanner.studentplanner.tables.CourseworkTable;
import com.studentplanner.studentplanner.utils.BoundTimePickerDialog;
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
    private final CustomTimePicker deadlineCustomTimePicker = new CustomTimePicker(LocalTime.now().plusHours(1).getHour(), LocalTime.now().getMinute());

    private AutoCompleteTextView txtPriority;
    private AutoCompleteTextView txtModules;

    private AutoCompleteTextView txtDeadline;
    private AutoCompleteTextView txtDeadlineTime;
    private TextInputLayout txtTitle;
    private TextInputLayout txtDescription;
    private int selectedModuleID;
    final String SELECT_PRIORITY = "Select Priority";
    private DatabaseHelper db;
    private BoundTimePickerDialog deadlineTimePicker;
    private Validation form;
    private ActivityAddCourseworkBinding binding;

//    private void deadlineSetup(BoundTimePickerDialog deadlineTimePicker, LocalDate localDate) {
//
//        Helper.setMinTimeStatus(deadlineTimePicker, localDate);
//    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_coursework);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding = ActivityAddCourseworkBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = DatabaseHelper.getInstance(this);
        form = new Validation(this);

        txtPriority = binding.txtPriority;
        txtDeadline = binding.txtDeadline;
        txtDeadlineTime = binding.txtDeadlineTime;
        txtModules = binding.txtModule;
        txtModules.setText(R.string.select_module);
        txtPriority.setText(SELECT_PRIORITY);
        txtTitle = binding.txtTitle;
        txtDescription = binding.txtDescription;
        setTimePicker();


        txtDeadlineTime.setText(Helper.showFormattedDBTime(LocalTime.now().plusHours(1).toString(), this));

        Dropdown.getStringArray(txtPriority, this, R.array.priority_array);
        txtDeadline.setText(Helper.formatDate(CalendarUtils.getCurrentDate().toString()));

        if (getIntent().getStringExtra(CourseworkTable.COLUMN_DEADLINE) != null) {
            txtDeadline.setText(getIntent().getStringExtra(CourseworkTable.COLUMN_DEADLINE));

        }
        setupDatePicker();
        getModulesList();


        binding.btnAddCoursework.setOnClickListener(v -> {

            if (form.validateAddCourseworkForm(getCourseworkErrorFields())) {
                if (db.addCoursework(getCourseworkDetails())) {
                    Helper.longToastMessage(this,"Coursework Added");
                    setResult(RESULT_OK);
                    finish();
                }

            }


        });


    }
    private Coursework getCourseworkErrorFields(){
        TextInputLayout txtModuleError = binding.txtModuleError;
        TextInputLayout errorPriority = binding.txtPriorityError;
        TextInputLayout timeError = binding.txtDeadlineTimeError;
        Coursework coursework = new Coursework(txtTitle, txtModuleError, errorPriority);
        coursework.setTxtDeadline(txtDeadline);
        coursework.setTxtDeadlineTime(txtDeadlineTime);
        coursework.setTxtDeadlineTimeError(timeError);
        return coursework;
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
        LocalDate selectedDate = Helper.formatDate(year, month, day);



        txtDeadline.setText(Helper.formatDate(String.valueOf(selectedDate)));
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
                deadlineTimePicker = new BoundTimePickerDialog(this, this, deadlineCustomTimePicker.getSelectedHour(), deadlineCustomTimePicker.getSelectedMinute());
                String deadlineDate  = Helper.convertFUllDateToYYMMDD(Helper.trimStr(txtDeadline));
                LocalDate deadline = LocalDate.parse(deadlineDate);
                LocalDate today = CalendarUtils.getCurrentDate();
                LocalDate date = deadline.isEqual(today)? today: deadline;
                deadlineSetup(deadlineTimePicker, date);
                deadlineTimePicker.show();
            }

            return false;
        });
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
        deadlineCustomTimePicker.setSelectedHour(selectedHour);
        deadlineCustomTimePicker.setSelectedMinute(selectedMinute);
        String selectedTime = String.format(Locale.getDefault(), getString(R.string.time_format_database), selectedHour, selectedMinute);
        txtDeadlineTime.setText(Helper.formatTime(selectedTime));

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) finish();
        return true;
    }

}