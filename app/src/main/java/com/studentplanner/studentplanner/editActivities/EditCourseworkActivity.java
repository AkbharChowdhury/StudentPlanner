package com.studentplanner.studentplanner.editActivities;

import static com.studentplanner.studentplanner.utils.Helper.deadlineSetup;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TimePicker;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import com.studentplanner.studentplanner.models.ImageHandler;
import com.studentplanner.studentplanner.models.Module;
import com.studentplanner.studentplanner.tables.CourseworkTable;
import com.studentplanner.studentplanner.utils.BoundTimePickerDialog;
import com.studentplanner.studentplanner.utils.CalendarUtils;
import com.studentplanner.studentplanner.utils.DatePickerFragment;
import com.studentplanner.studentplanner.utils.Dropdown;
import com.studentplanner.studentplanner.utils.Helper;
import com.studentplanner.studentplanner.utils.Validation;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Locale;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class EditCourseworkActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, EasyPermissions.PermissionCallbacks {
    private final int STORAGE_PERMISSION_CODE = 1;

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
    private ImageView courseworkImage;
    private Bitmap imageToStore;
    private boolean deleteImage = false;


    private final ActivityResultLauncher<Intent> imageActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK) {
            if (result.getData() != null) {
                try {
                    imageToStore = MediaStore.Images.Media.getBitmap(getContentResolver(), result.getData().getData());
                    courseworkImage.setImageBitmap(imageToStore);
                    binding.btnRemovePicture.setVisibility(View.VISIBLE);
                    deleteImage = false;


                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }

        }

    });

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

            if (form.validateEditCourseworkForm(getCourseworkErrorFields())) {
                if (db.updateCoursework(getCourseworkDetails(), deleteImage)) {
                    Helper.longToastMessage(this, "Coursework Updated");
                    setResult(RESULT_OK);
                    finish();
                }
            }
        });
        binding.btnRemovePicture.setOnClickListener(v -> handleRemoveClick());
        courseworkImage.setOnClickListener(v -> openFilesApp());
        binding.titleTextInputEditText.setOnKeyListener((v, keyCode, event) -> {
            Helper.characterCounter(txtTitle, getApplicationContext());
            return false;
        });


    }

    private Coursework getCourseworkErrorFields() {
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

        Dropdown.getStringArray(txtPriority, this, R.array.priority_array);
        txtDeadlineError = binding.txtDeadlineError;
        checkBoxCompleted = binding.checkboxEditCoursework;
        courseworkImage = binding.imgCoursework;


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

            showCourseworkImage(coursework.getByteImage());


        }

    }

    private void showCourseworkImage(byte[] image) {
        if (image != null) {
            courseworkImage.setImageBitmap(ImageHandler.decodeBitmapByteArray(image));
            binding.btnRemovePicture.setVisibility(View.VISIBLE);

        } else {
            courseworkImage.setImageResource(R.drawable.ic_placeholder_image);
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


    @SuppressLint("ClickableViewAccessibility")
    private void setTimePicker() {
        txtDeadlineTime.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                deadlineTimePicker = new BoundTimePickerDialog(this, this, deadlineCustomTimePicker.getSelectedHour(), deadlineCustomTimePicker.getSelectedMinute());
                String deadlineDate = Helper.convertFUllDateToYYMMDD(Helper.trimStr(txtDeadline));
                LocalDate deadline = LocalDate.parse(deadlineDate);
                LocalDate today = CalendarUtils.getCurrentDate();
                LocalDate date = deadline.isEqual(today) ? today : deadline;
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

        if (imageToStore != null) {
            coursework.setImage(imageToStore);


        }

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
        if (item.getItemId() == android.R.id.home) {
            setResult(RESULT_OK);
            finish();
        }

        if (item.getItemId() == R.id.ic_delete) {
            new AlertDialog.Builder(this)
                    .setMessage("You can't undo this").setCancelable(false)
                    .setTitle(getString(R.string.delete_coursework_title))
                    .setPositiveButton(getString(R.string.yes), (dialog, which) -> {
                        int id = getIntent().getIntExtra(CourseworkTable.COLUMN_ID, 0);
                        if (db.deleteRecord(CourseworkTable.TABLE_NAME, CourseworkTable.COLUMN_ID, id)) {
                            Helper.longToastMessage(this, getString(R.string.delete_coursework));
                            setResult(RESULT_OK);
                            finish();
                        }


                    })
                    .setNegativeButton(getString(R.string.no), (dialog, which) -> dialog.cancel()).create().show();

        }
        return super.onOptionsItemSelected(item);
    }


    private void handleRemoveClick() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.delete_image_title))
                .setPositiveButton(getString(R.string.yes), (dialog, which) -> {
                    imageToStore = null;
                    deleteImage = true;
                    courseworkImage.setImageResource(R.drawable.ic_placeholder_image);
                    binding.btnRemovePicture.setVisibility(View.GONE);

                })
                .setNegativeButton(getString(R.string.no), (dialog, which) -> dialog.cancel()).create().show();

    }


    @AfterPermissionGranted(STORAGE_PERMISSION_CODE)
    private void openFilesApp() {
        String[] perms = {Manifest.permission.READ_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            openImageGallery();
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.permissions_rationale), STORAGE_PERMISSION_CODE, perms);

        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    private void openImageGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent = Intent.createChooser(intent, getString(R.string.select_image));
        imageActivityResultLauncher.launch(intent);
    }


}