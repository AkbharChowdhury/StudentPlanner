package com.studentplanner.studentplanner.addActivities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.studentplanner.studentplanner.DatabaseHelper;
import com.studentplanner.studentplanner.R;
import com.studentplanner.studentplanner.models.Module;
import com.studentplanner.studentplanner.tables.ModuleTable;

import java.util.List;

public class AddModuleTeacherActivity extends AppCompatActivity {
    private AutoCompleteTextView txtModules;
    private DatabaseHelper db;
    List<Module> moduleList;
    private final ActivityResultLauncher<Intent> startForResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {

            }
    );


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_module_teacher);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = DatabaseHelper.getInstance(this);
        moduleList = db.getModuleClassesAdd();
        txtModules = findViewById(R.id.txtModuleAddTeacher);

        txtModules.setText(R.string.select_module);
        getModulesList();

    }
    private void getModulesList() {


        final List<String> items = Module.populateDropdown(moduleList);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_item, items);
        txtModules.setAdapter(adapter);
        txtModules.setOnItemClickListener((parent, view, position, id) -> startForResult.launch(moduleIntent(moduleList.get(position).getModuleID())));
    }


    private Intent moduleIntent(int moduleID){
        Intent intent = new Intent(this, AddModuleTeacherCheckboxActivity.class);
        intent.putExtra(ModuleTable.COLUMN_ID, moduleID);
        return intent;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) finish();
        return true;
    }

}