package com.studentplanner.studentplanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;

import com.google.android.material.textfield.TextInputLayout;
import com.studentplanner.studentplanner.enums.DatePickerType;
import com.studentplanner.studentplanner.fragments.SemesterFragment;
import com.studentplanner.studentplanner.models.Semester;
import com.studentplanner.studentplanner.tables.SemesterTable;
import com.studentplanner.studentplanner.utils.CalendarUtils;
import com.studentplanner.studentplanner.utils.DatePickerFragment;
import com.studentplanner.studentplanner.utils.Helper;
import com.studentplanner.studentplanner.utils.Validation;

import java.time.LocalDate;

public class EditSemesterActivity extends AppCompatActivity  implements DatePickerDialog.OnDateSetListener{
    private DatabaseHelper db;
    private AutoCompleteTextView txtStartDate;
    private AutoCompleteTextView txtEndDate;
    private DatePickerType type;
    private DatePickerFragment datePickerStart;
    private DatePickerFragment datePickerEnd;
    private TextInputLayout txtName;
    Validation form;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_semester);
        db = DatabaseHelper.getInstance(this);
        form = new Validation(this);
        findTextFields();
        setUpDatePickers();
        setupFields();

        findViewById(R.id.btn_edit_semester).setOnClickListener(v -> {

            if (form.validateSemesterForm(txtName)){
                if (db.updateSemester(getSemesterDetails())) {
                    Helper.setRedirectMessageFragment(this, SemesterFragment.class, "Semester Updated");
                }

            }
        });
    }

    private Semester getSemesterDetails() {
        return  new Semester(
                getIntent().getIntExtra(SemesterTable.COLUMN_ID, 0),
                Helper.trimStr(txtName),
                LocalDate.parse(Helper.convertFUllDateToYYMMDD(txtStartDate.getEditableText().toString())),
                LocalDate.parse(Helper.convertFUllDateToYYMMDD(txtEndDate.getEditableText().toString()))
        );

    }


    private void setupFields() {
        final String SELECTED_ID = SemesterTable.COLUMN_ID;
        if (getIntent().hasExtra(SELECTED_ID)) {

            int id = getIntent().getIntExtra(SELECTED_ID, 0);
            Semester semester = db.getSelectedSemester(id);
            txtName.getEditText().setText(semester.getName());
            txtStartDate.setText(Helper.formatDate(semester.getStart().toString()));
            txtEndDate.setText(Helper.formatDate(semester.getEnd().toString()));
            txtName.getEditText().setText(semester.getName());
        }

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
                    .setMessage("Doing so will delete all associated classes with this semester").setCancelable(false)
                    .setTitle("Are you sure you want to delete this semester?")
                    .setPositiveButton(getString(R.string.yes), (dialog, which) -> {
                        int id = getIntent().getIntExtra(SemesterTable.COLUMN_ID, 0);
                        if (db.deleteRecord(SemesterTable.TABLE_NAME, SemesterTable.COLUMN_ID, id)){
                            Helper.setRedirectMessageFragment(this, SemesterFragment.class, "Semester deleted");
                        }
                    })
                    .setNegativeButton(getString(R.string.no), (dialog, which) -> dialog.cancel()).create().show();

        }
        return super.onOptionsItemSelected(item);
    }
    private void findTextFields() {
        txtName =  findViewById(R.id.txtSemesterEdit);
        txtStartDate = findViewById(R.id.txtStartDateEdit);
        txtEndDate = findViewById(R.id.txtEndDateEdit);
    }
    private void setUpDatePickers() {
        setStartDatePicker();
        setEndDatePicker();

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
    private void createDatePickerConstraint(DatePickerFragment datePickerStart) {
        String endDate = Helper.convertFUllDateToYYMMDD(txtEndDate.getEditableText().toString());
        String startDate = Helper.convertFUllDateToYYMMDD(txtStartDate.getEditableText().toString());
        datePickerStart.setDatePickerStartEnd(LocalDate.parse(startDate), LocalDate.parse(endDate));
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onDateSet(DatePicker d, int year, int month, int day) {

        LocalDate date = Helper.formatDate(year, month, day);
        String formattedDate = Helper.formatDate(String.valueOf(date));
        switch (type) {
            case START_DATE:
                txtStartDate.setText(formattedDate);
                break;
            case END_DATE:
                txtEndDate.setText(formattedDate);
                break;
        }

    }


}