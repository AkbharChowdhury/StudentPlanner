package com.studentplanner.studentplanner;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.studentplanner.studentplanner.models.Teacher;
import com.studentplanner.studentplanner.utils.Helper;
import com.studentplanner.studentplanner.utils.Validation;

public class AddTeacherActivity extends AppCompatActivity {
    private DatabaseHelper db;
    private Validation form;
    private TextInputLayout txtFirstName;
    private TextInputLayout txtLastName;
    private TextInputLayout txtEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_teacher);
        db = DatabaseHelper.getInstance(this);
        form = new Validation(this, db);
        txtFirstName = findViewById(R.id.txtFirstnameTeacher);
        txtLastName = findViewById(R.id.txtLastnameTeacher);
        txtEmail = findViewById(R.id.txtEmailTeacher);

        findViewById(R.id.btn_add_teacher).setOnClickListener(v -> {
            Teacher teacher = new Teacher(txtFirstName, txtLastName, txtEmail);
            if (form.validateAddTeacherForm(teacher)){
                if (db.addTeacher(getTeacherDetails())){
                    Helper.setRedirectMessageFragment(this, TeacherFragment.class, "Teacher Added");


                }
            }



        });
    }


    private Teacher getTeacherDetails() {
        return new Teacher(Helper.trimStr(txtFirstName), Helper.trimStr(txtLastName),Helper.trimStr(txtEmail) );

    }
}