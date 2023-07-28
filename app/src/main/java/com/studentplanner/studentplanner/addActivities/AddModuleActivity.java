package com.studentplanner.studentplanner.addActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.textfield.TextInputLayout;
import com.studentplanner.studentplanner.DatabaseHelper;
import com.studentplanner.studentplanner.R;
import com.studentplanner.studentplanner.databinding.ActivityAddModuleBinding;
import com.studentplanner.studentplanner.fragments.ModuleFragment;
import com.studentplanner.studentplanner.models.Module;
import com.studentplanner.studentplanner.utils.Helper;
import com.studentplanner.studentplanner.utils.Validation;

public class AddModuleActivity extends AppCompatActivity {
    private DatabaseHelper db;
    private TextInputLayout txtModuleCode;
    private TextInputLayout txtModuleName;
    private Validation form;
    private ActivityAddModuleBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_module);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding = ActivityAddModuleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = DatabaseHelper.getInstance(this);
        form = new Validation(this, db);

        txtModuleCode = binding.txtModuleCode;
        txtModuleName = binding.txtModuleName;

        binding.btnAddModule.setOnClickListener(v -> {
            Module module = getModuleDetails();
            if (!form.validateAddModuleForm(module)) return;
            if (db.addModule(module)) {
                Helper.longToastMessage(this, getString(R.string.module_added));
                setResult(RESULT_OK);
                finish();
            }


        });

    }

    private Module getModuleDetails() {
        return new Module(Helper.trimStr(txtModuleCode), Helper.trimStr(txtModuleName));

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) finish();
        return true;
    }
}