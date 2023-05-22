package com.studentplanner.studentplanner;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputLayout;
import com.studentplanner.studentplanner.models.Coursework;
import com.studentplanner.studentplanner.models.Module;
import com.studentplanner.studentplanner.tables.CourseworkTable;
import com.studentplanner.studentplanner.utils.CalendarUtils;
import com.studentplanner.studentplanner.utils.DatePickerFragment;
import com.studentplanner.studentplanner.utils.Dropdown;
import com.studentplanner.studentplanner.utils.Helper;
import com.studentplanner.studentplanner.utils.TimePickerFragment;
import com.studentplanner.studentplanner.utils.Validation;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

public class EditCourseworkActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private DatabaseHelper db;
    private Validation form;
    private AutoCompleteTextView txtPriority;
    private AutoCompleteTextView txtModules;

    private AutoCompleteTextView txtDeadline;
    private AutoCompleteTextView txtDeadlineTime;
    private TextInputLayout txtTitle;
    private TextInputLayout txtDescription;
    private int selectedModuleID;
    private TextInputLayout txtDeadlineError;
    private MaterialCheckBox checkBoxCompleted;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_coursework);
        db = DatabaseHelper.getInstance(this);
        form = new Validation(this);
        initFields();

        getModulesList();

        setupFields();
        setupDatePicker();
        setTimePicker();


        findViewById(R.id.btn_edit_coursework).setOnClickListener(v -> {
            if (form.validateEditCourseworkForm(txtTitle, txtDeadline, txtDeadlineError)){
                if (db.updateCoursework(getCourseworkDetails())) {
                    Helper.longToastMessage(this,"Coursework Updated");
                    Helper.setRedirectMessageFragment(this, CourseworkFragment.class, "Coursework Updated");
                }
            }
        });

    }

    private void initFields() {
        txtPriority = findViewById(R.id.txtPriorityEdit);
        txtDeadline = findViewById(R.id.txtDeadlineEdit);
        txtDeadlineTime = findViewById(R.id.txtDeadlineTimeEdit);
        txtModules = findViewById(R.id.txtModuleEdit);
        txtTitle = findViewById(R.id.txtTitleEdit);
        txtDescription = findViewById(R.id.txtDescriptionEdit);
        Dropdown.getStringArray(txtPriority, this,R.array.priority_array );
        txtDeadlineError = findViewById(R.id.txtDeadlineError);
        checkBoxCompleted = findViewById(R.id.checkbox_edit_coursework);



    }

    private void setupFields() {
        final String SELECTED_ID = CourseworkTable.COLUMN_ID;

        if (getIntent().hasExtra(SELECTED_ID)) {
            int id = getIntent().getIntExtra(SELECTED_ID, 0);
            Coursework coursework = db.getSelectedCoursework(id);
            txtTitle.getEditText().setText(coursework.getTitle());
            txtDescription.getEditText().setText(coursework.getDescription());
            txtPriority.setText(txtPriority.getAdapter().getItem(Dropdown.getPriorityNumber(coursework.getPriority(), this)).toString(), false);
            txtDeadline.setText(Helper.formatDate(coursework.getDeadline()));
            txtDeadlineTime.setText(Helper.showFormattedDBTime(coursework.getDeadlineTime(), this));
            txtModules.setText(txtModules.getAdapter().getItem(Dropdown.getModuleID(coursework.getModuleID(), db.getModules())).toString(), false);
            selectedModuleID = coursework.getModuleID();
            checkBoxCompleted.setChecked(coursework.isCompleted());



        }

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


    @Override
    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
        String selectedTime = String.format(Locale.getDefault(), getString(R.string.time_format_database), selectedHour, selectedMinute);
        txtDeadlineTime.setText(Helper.formatTime(selectedTime));

    }
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onDateSet(DatePicker d, int year, int month, int day) {
        LocalDate date = Helper.formatDate(year, month, day);
        txtDeadline.setText(Helper.formatDate(String.valueOf(date)));
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setTimePicker() {
        txtDeadlineTime.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                new TimePickerFragment().show(getSupportFragmentManager(), "timePicker");
            }


            return false;
        });
    }

    private void getModulesList() {

        final List<Module> moduleList = db.getModules();
        final List<String> items = Module.populateDropdown(moduleList);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_item, items);
        txtModules.setAdapter(adapter);
        txtModules.setOnItemClickListener((parent, view, position, id) -> selectedModuleID = moduleList.get(position).getModuleID());
    }


    private Coursework getCourseworkDetails() {

        Coursework coursework = new Coursework(
                getIntent().getIntExtra(CourseworkTable.COLUMN_ID, 0),
                selectedModuleID,
                Helper.trimStr(txtTitle),
                Helper.trimStr(txtDescription),
                Helper.trimStr(txtPriority),
                Helper.convertFUllDateToYYMMDD(Helper.trimStr(txtDeadline)),
                Helper.convertFormattedTimeToDBFormat(txtDeadlineTime.getText().toString())

        );
        coursework.setCompleted(checkBoxCompleted.isChecked());
        return coursework;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.delete_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.ic_delete){
            new AlertDialog.Builder(this)
                    .setMessage("You can't undo this").setCancelable(false)
                    .setTitle("Are you sure you want to delete this coursework?")
                    .setPositiveButton(getString(R.string.yes), (dialog, which) -> {
                        int id = getIntent().getIntExtra(CourseworkTable.COLUMN_ID, 0);
                        if (db.deleteRecord(CourseworkTable.TABLE_NAME, CourseworkTable.COLUMN_ID, id)){
                            Helper.setRedirectMessageFragment(this, CourseworkFragment.class, "coursework deleted");
                        }


                    })
                    .setNegativeButton(getString(R.string.no), (dialog, which) -> dialog.cancel()).create().show();

        }
        return super.onOptionsItemSelected(item);
    }
}