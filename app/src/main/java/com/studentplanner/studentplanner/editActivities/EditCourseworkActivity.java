package com.studentplanner.studentplanner.editActivities;

import static com.studentplanner.studentplanner.utils.Helper.deadlineSetup;

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
import com.studentplanner.studentplanner.DatabaseHelper;
import com.studentplanner.studentplanner.R;
import com.studentplanner.studentplanner.databinding.ActivityEditCourseworkBinding;
import com.studentplanner.studentplanner.models.Coursework;
import com.studentplanner.studentplanner.models.CustomTimePicker;
import com.studentplanner.studentplanner.models.Module;
import com.studentplanner.studentplanner.tables.CourseworkTable;
import com.studentplanner.studentplanner.utils.BoundTimePickerDialog;
import com.studentplanner.studentplanner.utils.CalendarUtils;
import com.studentplanner.studentplanner.utils.DatePickerFragment;
import com.studentplanner.studentplanner.utils.Dropdown;
import com.studentplanner.studentplanner.utils.Helper;
import com.studentplanner.studentplanner.utils.Validation;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Locale;

public class EditCourseworkActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private CustomTimePicker deadlineCustomTimePicker;

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

    private ActivityEditCourseworkBinding binding;
    private BoundTimePickerDialog deadlineTimePicker;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_coursework);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(R.string.edit_coursework);

        binding = ActivityEditCourseworkBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        db = DatabaseHelper.getInstance(this);
        form = new Validation(this);
        initFields();

        getModulesList();

        setupFields();
        setupDatePicker();
        setTimePicker();


        binding.btnEditCoursework.setOnClickListener(v -> {

            if (form.validateEditCourseworkForm(getCourseworkErrorFields())){
                Helper.longToastMessage(this, "passed");
                if (db.updateCoursework(getCourseworkDetails())) {
                    Helper.longToastMessage(this,"Coursework Updated");
                    setResult(RESULT_OK);
                    finish();
                }
            }
        });

    }

    private Coursework getCourseworkErrorFields(){
        Coursework coursework = new Coursework();

        coursework.setTxtDeadlineTime(txtDeadlineTime);
        coursework.setTxtDeadlineTimeError(binding.txtDeadlineTimeError);
        coursework.setTxtDeadline(txtDeadline);
        coursework.setTxtDeadlineError(txtDeadlineError);
        coursework.setTxtTitle(txtTitle);
        return coursework;
    }

    private void initFields() {
        txtPriority = binding.txtPriority;
        txtDeadline = binding.txtDeadline;
        txtDeadlineTime = binding.txtDeadlineTime;
        txtModules = binding.txtModule;
        txtTitle = binding.txtTitle;
        txtDescription = binding.txtDescription;

        Dropdown.getStringArray(txtPriority, this,R.array.priority_array );
        txtDeadlineError = binding.txtDeadlineError;
        checkBoxCompleted = binding.checkboxEditCoursework;



    }

    private void setupFields() {
        final String SELECTED_ID = CourseworkTable.COLUMN_ID;

        if (getIntent().hasExtra(SELECTED_ID)) {
            int id = getIntent().getIntExtra(SELECTED_ID, 0);
            Coursework coursework = db.getSelectedCoursework(id);
            txtTitle.getEditText().setText(coursework.getTitle());
            txtDescription.getEditText().setText(coursework.getDescription());
            txtPriority.setText(txtPriority.getAdapter().getItem(Dropdown.getSelectedStringArrayNumber(coursework.getPriority(), this, R.array.priority_array)).toString(), false);
            txtDeadline.setText(Helper.formatDate(coursework.getDeadline()));
            txtDeadlineTime.setText(Helper.showFormattedDBTime(coursework.getDeadlineTime(), this));
            txtModules.setText(txtModules.getAdapter().getItem(Dropdown.getModuleID(coursework.getModuleID(), db.getModules())).toString(), false);
            selectedModuleID = coursework.getModuleID();
            checkBoxCompleted.setChecked(coursework.isCompleted());


            LocalTime deadlineTime = LocalTime.parse(coursework.getDeadlineTime());

            deadlineCustomTimePicker = new CustomTimePicker(deadlineTime.getHour(), deadlineTime.getMinute());




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


//    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
////        String selectedTime = String.format(Locale.getDefault(), getString(R.string.time_format_database), selectedHour, selectedMinute);
////        txtDeadlineTime.setText(Helper.formatTime(selectedTime));
//
//
//    }
    @Override
    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
        deadlineCustomTimePicker.setSelectedHour(selectedHour);
        deadlineCustomTimePicker.setSelectedMinute(selectedMinute);
        String selectedTime = String.format(Locale.getDefault(), getString(R.string.time_format_database), selectedHour, selectedMinute);
        txtDeadlineTime.setText(Helper.formatTime(selectedTime));

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onDateSet(DatePicker d, int year, int month, int day) {
        LocalDate date = Helper.formatDate(year, month, day);
        txtDeadline.setText(Helper.formatDate(String.valueOf(date)));
    }

//
//    @SuppressLint("ClickableViewAccessibility")
//    private void setTimePicker() {
//        txtDeadlineTime.setOnTouchListener((view, motionEvent) -> {
//            if (motionEvent.getAction() == MotionEvent.ACTION_UP){
//                new TimePickerFragment().show(getSupportFragmentManager(), "timePicker");
//            }
//
//
//            return false;
//        });
//    }

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
        if (item.getItemId() == android.R.id.home) finish();

        if (item.getItemId() == R.id.ic_delete){
            new AlertDialog.Builder(this)
                    .setMessage("You can't undo this").setCancelable(false)
                    .setTitle("Are you sure you want to delete this coursework?")
                    .setPositiveButton(getString(R.string.yes), (dialog, which) -> {
                        int id = getIntent().getIntExtra(CourseworkTable.COLUMN_ID, 0);
                        if (db.deleteRecord(CourseworkTable.TABLE_NAME, CourseworkTable.COLUMN_ID, id)){
                            Helper.longToastMessage(this,"Coursework Deleted");
                            setResult(RESULT_OK);
                            finish();
                        }


                    })
                    .setNegativeButton(getString(R.string.no), (dialog, which) -> dialog.cancel()).create().show();

        }
        return super.onOptionsItemSelected(item);
    }
}