package com.studentplanner.studentplanner;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.material.textfield.TextInputLayout;
import com.studentplanner.studentplanner.fragments.ModuleFragment;
import com.studentplanner.studentplanner.models.Module;
import com.studentplanner.studentplanner.utils.Helper;
import com.studentplanner.studentplanner.utils.Validation;

public class AddModuleActivity extends AppCompatActivity {
    private DatabaseHelper db;
    private TextInputLayout txtModuleCode;
    private TextInputLayout txtModuleName;
    private Validation form;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_module);
        db = DatabaseHelper.getInstance(this);
        form = new Validation(this, db);
        txtModuleCode = findViewById(R.id.txtModuleCode);
        txtModuleName = findViewById(R.id.txtModuleName);

        findViewById(R.id.btn_add_module).setOnClickListener(v -> {
            if (form.validateAddModuleForm(new Module(txtModuleCode, txtModuleName))) {

                if (db.addModule(getModuleDetails())) {
                    Helper.setRedirectMessageFragment(this, ModuleFragment.class,"Module added");
                }
            }

        });

    }

    private Module getModuleDetails() {
        return new Module(Helper.trimStr(txtModuleCode), Helper.trimStr(txtModuleName));

    }
}