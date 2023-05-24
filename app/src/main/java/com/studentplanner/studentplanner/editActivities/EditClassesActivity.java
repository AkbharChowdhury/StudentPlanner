package com.studentplanner.studentplanner.editActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TimePicker;

import com.google.android.material.textfield.TextInputLayout;
import com.studentplanner.studentplanner.DatabaseHelper;
import com.studentplanner.studentplanner.MainActivity;
import com.studentplanner.studentplanner.R;
import com.studentplanner.studentplanner.enums.TimePickerType;
import com.studentplanner.studentplanner.models.Classes;
import com.studentplanner.studentplanner.models.Module;
import com.studentplanner.studentplanner.models.Semester;
import com.studentplanner.studentplanner.tables.ClassTable;
import com.studentplanner.studentplanner.utils.CalendarUtils;
import com.studentplanner.studentplanner.utils.Dropdown;
import com.studentplanner.studentplanner.utils.Helper;
import com.studentplanner.studentplanner.utils.TimePickerFragment;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Locale;

public class EditClassesActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_classes);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
        db = DatabaseHelper.getInstance(this);

        findFields();
        getModulesList();
        getSemesterList();
        setupFields();
        setUpTimePickers();

        findViewById(R.id.btn_edit_class).setOnClickListener(v -> {

            if (db.updateClass(getClassDetails())) {
                Helper.longToastMessage(this, "class updated");
            }


        });


    }

    private void setUpTimePickers() {
        setStartTimePicker();
        setEndTimePicker();


    }

    private Classes getClassDetails() {
        return new Classes(
                getIntent().getIntExtra(ClassTable.COLUMN_ID, 0),
                selectedModuleID,
                selectedSemesterID,
                CalendarUtils.getDOWNumber(Helper.trimStr(txtDays)),
                Helper.convertFormattedTimeToDBFormat(Helper.trimStr(txtStartTime)),
                Helper.convertFormattedTimeToDBFormat(Helper.trimStr(txtEndTime)),
                Helper.trimStr(txtRoom),
                Helper.trimStr(txtClassType)

        );
    }

    private void findFields() {
        txtDays = findViewById(R.id.txtDayEdit);
        txtModules = findViewById(R.id.txtModuleClassesEdit);
        txtSemester = findViewById(R.id.txtSemesterClassesEdit);
        txtClassType = findViewById(R.id.txtClassTypeEdit);

        txtStartTime = findViewById(R.id.txtStartTimeEdit);
        txtEndTime = findViewById(R.id.txtEndTimeEdit);
        txtRoom = findViewById(R.id.txtRoomEdit);
    }

    private void setupFields() {
        final String SELECTED_ID = ClassTable.COLUMN_ID;
        if (getIntent().hasExtra(SELECTED_ID)) {
            int id = getIntent().getIntExtra(SELECTED_ID, 0);
            Classes model = db.getSelectedClass(id);

            txtRoom.getEditText().setText(model.getRoom());
            Helper.getDays(txtDays, this);
            Helper.getStringArray(this, txtClassType, R.array.type_array);
            txtModules.setText(txtModules.getAdapter().getItem(Dropdown.getModuleID(model.getModuleID(), db.getModules())).toString(), false);
            txtSemester.setText(txtSemester.getAdapter().getItem(Dropdown.getSemesterID(model.getModuleID(), db.getSemester())).toString(), false);
            txtClassType.setText(txtClassType.getAdapter().getItem(Dropdown.getClassTypeNumber(model.getClassType(), this)).toString(), false);
            String classDay = DayOfWeek.of(model.getDow()).toString();

            txtDays.setText(
                    txtDays.getAdapter()
                            .getItem(Dropdown.setSelectedDay(classDay)).
                            toString(),
                    false);

            txtStartTime.setText(Helper.showFormattedDBTime(model.getStartTime(), this));
            txtEndTime.setText(Helper.showFormattedDBTime(model.getEndTime(), this));

            selectedModuleID = model.getModuleID();
            selectedSemesterID = model.getSemesterID();


        }
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.delete_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) finish();

        if (item.getItemId() == R.id.ic_delete) {
            new AlertDialog.Builder(this)
                    .setMessage("You can't undo this").setCancelable(false)
                    .setTitle("Are you sure you want to delete this class?")
                    .setPositiveButton(getString(R.string.yes), (dialog, which) -> {
                        int id = getIntent().getIntExtra(ClassTable.COLUMN_ID, 0);
                        if (db.deleteRecord(ClassTable.TABLE_NAME, ClassTable.COLUMN_ID, id)) {
                            Helper.setRedirectMessage(this, MainActivity.class, "Class deleted");
                        }


                    })
                    .setNegativeButton(getString(R.string.no), (dialog, which) -> dialog.cancel()).create().show();

        }
        return super.onOptionsItemSelected(item);
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




}