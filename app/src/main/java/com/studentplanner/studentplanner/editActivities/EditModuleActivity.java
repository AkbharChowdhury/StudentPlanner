package com.studentplanner.studentplanner.editActivities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.studentplanner.studentplanner.DatabaseHelper;
import com.studentplanner.studentplanner.R;
import com.studentplanner.studentplanner.models.Module;
import com.studentplanner.studentplanner.tables.ModuleTable;
import com.studentplanner.studentplanner.utils.Helper;
import com.studentplanner.studentplanner.utils.Validation;

public class EditModuleActivity extends AppCompatActivity {
    private DatabaseHelper db;
    private TextInputLayout txtModuleCode;
    private TextInputLayout txtModuleName;
    private Validation form;
    private String excludedModuleCode;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_module);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = DatabaseHelper.getInstance(this);
        txtModuleCode = findViewById(R.id.txtModuleCodeEdit);
        txtModuleName = findViewById(R.id.txtModuleNameEdit);
        form = new Validation(this, db);

        setupFields();
        findViewById(R.id.btn_edit_module).setOnClickListener(v -> {
            if (form.validateEditModuleForm(new Module(txtModuleCode, txtModuleName), excludedModuleCode)) {
                if (db.updateModule(getModuleDetails())) {
//                    Helper.setUpdatedStatus(true);
                    Helper.longToastMessage(this,"Module Updated");
                    setResult(RESULT_OK);
                    finish();
                }
            }
        });
    }

    private Module getModuleDetails() {
        return new Module(
                getIntent().getIntExtra(ModuleTable.COLUMN_ID, 0),
                Helper.trimStr(txtModuleCode),
                Helper.trimStr(txtModuleName));
    }

    private void setupFields() {
        final String SELECTED_ID = ModuleTable.COLUMN_ID;

        if (getIntent().hasExtra(SELECTED_ID)) {
            int id = getIntent().getIntExtra(SELECTED_ID, 0);
            Module model = db.getSelectedModule(id);
            txtModuleCode.getEditText().setText(model.getModuleCode());
            txtModuleName.getEditText().setText(model.getModuleName());
            excludedModuleCode = model.getModuleCode();

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
                    .setMessage("Doing so will delete all associated coursework and classes with this module").setCancelable(false)
                    .setTitle("Are you sure you want to delete this module?")
                    .setPositiveButton(getString(R.string.yes), (dialog, which) -> {
                        int id = getIntent().getIntExtra(ModuleTable.COLUMN_ID, 0);
                        if (db.deleteRecord(ModuleTable.TABLE_NAME, ModuleTable.COLUMN_ID, id)){
//                            Helper.setRedirectMessageFragment(this, ModuleFragment.class, "Module deleted");
                        }


                    })
                    .setNegativeButton(getString(R.string.no), (dialog, which) -> dialog.cancel()).create().show();

        }
        return super.onOptionsItemSelected(item);
    }

}
