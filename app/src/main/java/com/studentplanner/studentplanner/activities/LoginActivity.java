package com.studentplanner.studentplanner.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.studentplanner.studentplanner.DatabaseHelper;
import com.studentplanner.studentplanner.MainActivity;
import com.studentplanner.studentplanner.R;
import com.studentplanner.studentplanner.utils.AccountPreferences;
import com.studentplanner.studentplanner.utils.Encryption;
import com.studentplanner.studentplanner.utils.Helper;
import com.studentplanner.studentplanner.utils.Validation;

public class LoginActivity extends AppCompatActivity {
    private final Activity CURRENT_ACTIVITY = LoginActivity.this;
    private Context context;
    private DatabaseHelper db;

    private TextInputLayout txtEmail;
    private TextInputLayout txtPassword;
    private Validation form;
    private TextView lblLoginError;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = getApplicationContext();
        db = DatabaseHelper.getInstance(context);

        Helper.getIntentMessage(context, getIntent().getExtras());

        txtEmail = findViewById(R.id.txtEmail);

        txtPassword = findViewById(R.id.txtPassword);
        txtEmail.getEditText().setText("tom@gmail.com");
        txtPassword.getEditText().setText("password");

        findViewById(R.id.btn_register_link).setOnClickListener(v -> Helper.goToActivity(this, RegisterActivity.class));
        lblLoginError = findViewById(R.id.lbl_login_error);
        lblLoginError.setVisibility(View.INVISIBLE);
        findViewById(R.id.btn_login).setOnClickListener(v -> {
            String email = Helper.trimStr(txtEmail);
            String password = Helper.trimStr(txtPassword, false);
            if (db.isAuthorised(email, Encryption.encode(password))) {
                lblLoginError.setVisibility(View.INVISIBLE);
                configUserDetails(email);
                return;
            }
            lblLoginError.setVisibility(View.VISIBLE);


        });

    }


    private void configUserDetails(String email) {
        AccountPreferences.setLoginShredPref(context, db.getStudentID(email));
        Helper.goToActivity(CURRENT_ACTIVITY, MainActivity.class);

    }
}