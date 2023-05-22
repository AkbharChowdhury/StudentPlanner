package com.studentplanner.studentplanner;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputLayout;
import com.studentplanner.studentplanner.models.Student;
import com.studentplanner.studentplanner.utils.AlertDialogFragment;
import com.studentplanner.studentplanner.utils.Helper;
import com.studentplanner.studentplanner.utils.PasswordValidator;
import com.studentplanner.studentplanner.utils.Validation;

public class RegisterActivity extends AppCompatActivity {
    private TextInputLayout txtFirstName;
    private TextInputLayout txtLastName;
    private TextInputLayout txtEmail;
    private TextInputLayout txtPhone;
    private TextInputLayout txtPassword;
    private DatabaseHelper db;
    private Context context;
    private Validation form;
    private ProgressBar progressBar;
    private MaterialCheckBox terms;
    private AlertDialogFragment alertDialogFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        context = getApplicationContext();

        db = DatabaseHelper.getInstance(context);
        form = new Validation(context, db);
        alertDialogFragment = new AlertDialogFragment(this);
        findTextFields();


        terms = findViewById(R.id.checkbox_terms_conditions);
        findViewById(R.id.btn_terms_conditions).setOnClickListener(v -> alertDialogFragment.showTermsAndConditions());



        findViewById(R.id.btn_register).setOnClickListener(v -> {

            Student student = new Student(txtFirstName, txtLastName, txtEmail, txtPassword);
            student.setTxtPhone(txtPhone);
            Log.d("OO", String.valueOf(form.validateRegisterForm(student)));
            if (form.validateRegisterForm(student)) {
                if (!terms.isChecked()){
                    alertDialogFragment.showTermsPolicyError();
                    return;
                }

                if (db.registerUser(getStudentDetails())) {
                    Helper.setRedirectMessage(this, LoginActivity.class, getString(R.string.account_created));
                    return;
                }
                Helper.longToastMessage(context, getString(R.string.create_account_error));

            }

        });
    }

    private void findTextFields() {
        progressBar = findViewById(R.id.progressBar);
        txtFirstName = findViewById(R.id.txtFirstname);
        txtLastName = findViewById(R.id.txtLastname);
        txtEmail = findViewById(R.id.txtEmail);
        txtPhone = findViewById(R.id.txtPhone);
        txtPassword = findViewById(R.id.txtPassword);

        findViewById(R.id.txtPasswordText).setOnKeyListener((v, keyCode, event) -> {
            String password = txtPassword.getEditText().getText().toString();
            updatePasswordStrengthView(password);
            return false;
        });
    }

    private Student getStudentDetails() {
        return new Student(
                Helper.trimStr(txtFirstName),
                Helper.trimStr(txtLastName),
                Helper.trimStr(txtEmail),
                Helper.trimStr(txtPhone),
                Helper.trimStr(txtPassword, false)
        );


    }

    private void updatePasswordStrengthView(String password) {
        int strength = 0;
        TextView strengthView = findViewById(R.id.password_strength);
        if (TextView.VISIBLE != strengthView.getVisibility()) return;
        if (PasswordValidator.is8Chars(password)) strength++;
        if (PasswordValidator.containsSpecialChar(password)) strength++;
        if (PasswordValidator.containsUpperCase(password)) strength++;
        if (PasswordValidator.containsLowerCase(password)) strength++;
        if (PasswordValidator.containsNumber(password)) strength++;
        new PasswordValidator(context, progressBar).getProgressBarStatus(strength, strengthView);

    }


}