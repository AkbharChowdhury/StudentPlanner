package com.studentplanner.studentplanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.studentplanner.studentplanner.models.Module;
import com.studentplanner.studentplanner.models.Teacher;
import com.studentplanner.studentplanner.tables.ModuleTable;
import com.studentplanner.studentplanner.utils.Helper;

import java.util.ArrayList;
import java.util.List;


public class EditModuleTeacherActivity extends AppCompatActivity {
    private DatabaseHelper db;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_module_teacher);
        db = DatabaseHelper.getInstance(this);
        setActivityTitle();

        List<String> teacherNames = getTeacher();
        String[] myTeachers = Helper.convertArrayListStringToStringArray(teacherNames);

        listView = findViewById(R.id.listview_data_edit);


        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_multiple_choice,
                myTeachers
        );
        listView.setAdapter(adapter);
        getSelectedTeacherEdited();



    }
    private void getSelectedTeacherEdited() {

        List<Teacher> teachers = db.getTeachers();
        int moduleID = getIntent().getIntExtra(ModuleTable.COLUMN_ID, 0);

        ArrayList<Integer> editedTeacherIDs = db.getModuleTeacherByModuleID(moduleID);
        ArrayList<Integer> allTeacherIDs = getIdList(teachers);
        for (int i = 0; i < teachers.size(); i++) {
            if ((editedTeacherIDs.contains(allTeacherIDs.get(i)))) {
                listView.setItemChecked(i, true);
            }

        }
    }
    private ArrayList<Integer> getIdList(List<Teacher> teachers) {
        ArrayList<Integer> idList = new ArrayList<>();
        for (Teacher t : teachers) {
            idList.add(t.getUserID());
        }
        return idList;

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
        List<Teacher> teachers = db.getTeachers();
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

                if(db.updateModuleTeacher(teacherIDs, moduleID)){

                    Helper.setRedirectMessageFragment(this, ModuleTeacherFragment.class, "teacher Updated ");


                }

            }

        }

        return super.onOptionsItemSelected(item);

    }
}