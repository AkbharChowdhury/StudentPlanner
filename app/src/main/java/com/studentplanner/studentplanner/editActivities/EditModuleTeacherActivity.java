package com.studentplanner.studentplanner.editActivities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.studentplanner.studentplanner.DatabaseHelper;
import com.studentplanner.studentplanner.R;
import com.studentplanner.studentplanner.models.Module;
import com.studentplanner.studentplanner.models.Teacher;
import com.studentplanner.studentplanner.tables.ModuleTable;
import com.studentplanner.studentplanner.utils.Helper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class EditModuleTeacherActivity extends AppCompatActivity {
    private DatabaseHelper db;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_module_teacher);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
        IntStream.range(0, teachers.size()).forEach(i ->  listView.setItemChecked(i, editedTeacherIDs.contains(allTeacherIDs.get(i))));
    }
    private ArrayList<Integer> getIdList(List<Teacher> teachers) {
        ArrayList<Integer> idList = new ArrayList<>();
        teachers.forEach(t -> idList.add(t.getUserID()));
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

        teachers.forEach(t-> teacherArray.add(String.format("%s %s", t.getFirstname(), t.getLastname())));
        return teacherArray;
    }
    private List<Integer> getSelectedTeacherIDList() {

        List<Integer> selectedTeacherIds = new ArrayList<>();
        List<Teacher> teacherList = db.getTeachers();
        IntStream.range(0, listView.getCount()).forEach(i ->  {
            if (listView.isItemChecked(i)) {
                selectedTeacherIds.add(teacherList.get(i).getUserID());
            }
        });



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
        if (item.getItemId() == android.R.id.home) finish();

        if (id == R.id.item_done) {

            List<Integer> teacherIDs = getSelectedTeacherIDList();
            if (teacherIDs.size() == 0) {
                new AlertDialog.Builder(this)
                        .setMessage( getString(R.string.delete_module_teacher_message))
                        .setCancelable(false)
                        .setTitle(getString(R.string.delete_module_teacher_title))
                        .setPositiveButton(getString(R.string.yes), (dialog, which) -> {
                            int moduleId = getIntent().getIntExtra(ModuleTable.COLUMN_ID, 0);
                            if (db.deleteSelectedTeacherModules(moduleId)){
                                Helper.longToastMessage(this, teacherRemoved(moduleId));
                                setResult(RESULT_OK);
                                finish();
                            }
                        })
                        .setNegativeButton(getString(R.string.no), (dialog, which) -> dialog.cancel()).create().show();

            } else {
                final String SELECTED_ID = ModuleTable.COLUMN_ID;
                int moduleID = getIntent().getIntExtra(SELECTED_ID, 0);

                if(db.updateModuleTeacher(teacherIDs, moduleID)){
                    Helper.longToastMessage(this, teacherUpdated(moduleID));
                    setResult(RESULT_OK);
                    finish();

                }

            }

        }

        return super.onOptionsItemSelected(item);

    }

    private String teacherRemoved(int moduleId){
        return "All teachers removed from " + db.getSelectedModule(moduleId).getModuleDetails();

    }
    private String teacherUpdated(int moduleId){
        return "Teacher updated for " + db.getSelectedModule(moduleId).getModuleDetails();

    }

}