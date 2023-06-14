package com.studentplanner.studentplanner.editActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.textfield.TextInputLayout;
import com.studentplanner.studentplanner.DatabaseHelper;
import com.studentplanner.studentplanner.R;
import com.studentplanner.studentplanner.databinding.ActivityEditTeacherBinding;
import com.studentplanner.studentplanner.models.Teacher;
import com.studentplanner.studentplanner.tables.ModuleTable;
import com.studentplanner.studentplanner.tables.TeacherTable;
import com.studentplanner.studentplanner.utils.Helper;
import com.studentplanner.studentplanner.utils.Validation;

public class EditTeacherActivity extends AppCompatActivity {
    private DatabaseHelper db;
    private Validation form;
    private TextInputLayout txtFirstName;
    private TextInputLayout txtLastName;
    private TextInputLayout txtEmail;
    private ActivityEditTeacherBinding binding;
    private String excludedEmail;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_teacher);
        binding = ActivityEditTeacherBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = DatabaseHelper.getInstance(this);
        form = new Validation(this, db);
        txtFirstName = binding.txtFirstname;
        txtLastName = binding.txtLastname;
        txtEmail = binding.txtEmail;
        setupFields();
        binding.btnEditTeacher.setOnClickListener(v -> {
            Teacher teacher = new Teacher(txtFirstName, txtLastName, txtEmail);

            if (form.validateAddTeacherForm(teacher)) {
                if (db.updateTeacher(getTeacherDetails())) {
                    Helper.longToastMessage(this,"Teacher Updated");
                    setResult(RESULT_OK);
                    finish();
                }
            }
        });

    }

    private Teacher getTeacherDetails() {
        return new Teacher(
                getIntent().getIntExtra(TeacherTable.COLUMN_ID, 0),
                Helper.trimStr(txtFirstName),
                Helper.trimStr(txtLastName),
                Helper.trimStr(txtEmail)
        );


    }




    private void setupFields() {
        final String SELECTED_ID = TeacherTable.COLUMN_ID;

        if (getIntent().hasExtra(SELECTED_ID)) {
            int id = getIntent().getIntExtra(SELECTED_ID, 0);
            Teacher model = db.getSelectedTeacher(id);
            txtFirstName.getEditText().setText(model.getFirstname());
            txtLastName.getEditText().setText(model.getLastname());
            final String email = model.getEmail();
            txtEmail.getEditText().setText(email);
            excludedEmail = email;

        }
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
                    .setMessage("Doing so will delete all associated modules taught by this teacher").setCancelable(false)
                    .setTitle("Are you sure you want to delete this teacher?")
                    .setPositiveButton(getString(R.string.yes), (dialog, which) -> {
                        int id = getIntent().getIntExtra(TeacherTable.COLUMN_ID, 0);
                        if (db.deleteRecord(TeacherTable.TABLE_NAME, TeacherTable.COLUMN_ID, id)){

                            Helper.longToastMessage(this,"Teacher Deleted");
                            setResult(RESULT_OK);
                            finish();
                        }


                    })
                    .setNegativeButton(getString(R.string.no), (dialog, which) -> dialog.cancel()).create().show();

        }
        return super.onOptionsItemSelected(item);
    }

}