package com.studentplanner.studentplanner.addActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.studentplanner.studentplanner.DatabaseHelper;
import com.studentplanner.studentplanner.R;
import com.studentplanner.studentplanner.fragments.ModuleTeacherFragment;
import com.studentplanner.studentplanner.fragments.SemesterFragment;
import com.studentplanner.studentplanner.models.Module;
import com.studentplanner.studentplanner.models.Teacher;
import com.studentplanner.studentplanner.tables.ModuleTable;
import com.studentplanner.studentplanner.utils.Helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AddModuleTeacherCheckboxActivity extends AppCompatActivity {
    private DatabaseHelper db;
    private ListView listView;
    private List<Teacher> teachers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_module_teacher_checkbox);
        db = DatabaseHelper.getInstance(this);
        teachers = db.getTeachers();
        setActivityTitle();

        List<String> teacherNames = getTeacher();
        String[] myTeachers = Helper.convertArrayListStringToStringArray(teacherNames);

        listView = findViewById(R.id.listview_data);


        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_multiple_choice,
                myTeachers
        );
        listView.setAdapter(adapter);


    }

    private void setActivityTitle() {
        final String SELECTED_ID = ModuleTable.COLUMN_ID;

        if (getIntent().hasExtra(SELECTED_ID)) {
            int id = getIntent().getIntExtra(SELECTED_ID, 0);
            Module module = db.getSelectedModule(id);
            setTitle(module.getModuleName());

        }
    }



    private List<String> getTeacher() {
        List<String> teacherArray = new ArrayList<>();
        for (Teacher teacher : teachers) {
            teacherArray.add(String.format("%s %s", teacher.getFirstname(), teacher.getLastname()));
        }
        return teacherArray;
    }
    private List<Integer> getSelectedTeacherIDList() {

        List<Integer> selectedTeacherIds = new ArrayList<>();
        List<Teacher> teacherList = db.getTeachers();
        for (int i = 0; i < listView.getCount(); i++) {
            if (listView.isItemChecked(i)) {
                selectedTeacherIds.add(teacherList.get(i).getUserID());
            }

        }

        return selectedTeacherIds;
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.checkbox_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.item_done) {

            List<Integer> teacherIDs = getSelectedTeacherIDList();
            if (teacherIDs.size() == 0) {
                Toast.makeText(this, "Please select at least one teacher from the list", Toast.LENGTH_SHORT).show();

            } else {
                final String SELECTED_ID = ModuleTable.COLUMN_ID;
                int moduleID = getIntent().getIntExtra(SELECTED_ID, 0);

                if(db.addModuleTeacher(teacherIDs, moduleID)){
                    Helper.longToastMessage(this, String.format(Locale.ENGLISH,"Teacher Added for %s", db.getSelectedModule(moduleID).getModuleDetails()));



                    setResult(RESULT_OK);
                    finish();

                }

            }

        }

        return super.onOptionsItemSelected(item);

    }
}