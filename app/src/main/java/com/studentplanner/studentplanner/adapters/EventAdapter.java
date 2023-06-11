package com.studentplanner.studentplanner.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.studentplanner.studentplanner.DatabaseHelper;
import com.studentplanner.studentplanner.R;
import com.studentplanner.studentplanner.databinding.EventRowBinding;
import com.studentplanner.studentplanner.editActivities.EditClassesActivity;
import com.studentplanner.studentplanner.editActivities.EditCourseworkActivity;
import com.studentplanner.studentplanner.enums.EventType;
import com.studentplanner.studentplanner.enums.Status;
import com.studentplanner.studentplanner.models.ClassRow;
import com.studentplanner.studentplanner.models.Classes;
import com.studentplanner.studentplanner.models.Coursework;
import com.studentplanner.studentplanner.models.CourseworkRow;
import com.studentplanner.studentplanner.models.Event;
import com.studentplanner.studentplanner.models.Module;
import com.studentplanner.studentplanner.models.ModuleTeacher;
import com.studentplanner.studentplanner.tables.ClassTable;
import com.studentplanner.studentplanner.tables.CourseworkTable;
import com.studentplanner.studentplanner.utils.Helper;

import org.apache.commons.text.WordUtils;

import java.util.List;

public class EventAdapter extends ArrayAdapter<Event> {

    private final Context context;
    private DatabaseHelper db;
    private final ActivityResultLauncher<Intent> startForResult;

    public EventAdapter(@NonNull Context context, List<Event> events, ActivityResultLauncher<Intent> startForResult) {
        super(context, 0, events);
        this.context = context;
        this.db = DatabaseHelper.getInstance(context);
        this.startForResult = startForResult;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Event event = getItem(position);
        @SuppressLint("ViewHolder")
        EventRowBinding binding = EventRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        binding.mainLayout.setOnClickListener(v -> {
            final int ID = event.getId();
            switch (event.getEventType()) {
                case COURSEWORK:
                    startForResult.launch(getCourseworkIntent(ID));
                    break;

                case CLASSES:
                    startForResult.launch(getClassesIntent(ID));
                    break;

            }
        });

        ImageView classesIcon = binding.eventIconClasses;
        ImageView courseworkIcon = binding.eventIconCoursework;
        int eventIcon = getEventIcon(event.getEventType());
        classesIcon.setImageResource(eventIcon);
        courseworkIcon.setImageResource(eventIcon);

        ConstraintLayout classLayout = binding.mainLayoutClasses;
        ConstraintLayout courseworkLayout = binding.mainLayoutCoursework;


        CourseworkRow courseworkRow = new CourseworkRow(binding);
        ClassRow classRow = new ClassRow(binding);

        switch (event.getEventType()) {
            case COURSEWORK:
                classLayout.setVisibility(View.GONE);
                getCourseworkDetails(event.getCoursework(), courseworkRow);
                break;
            case CLASSES:
                courseworkLayout.setVisibility(View.GONE);
                getClassDetails(event.getClasses(), classRow);

                break;
        }


        return binding.getRoot();
    }

    private void getClassDetails(final Classes classes, final ClassRow row) {

        final int moduleID = classes.getModuleID();

        List<ModuleTeacher> moduleTeacherList = db.getModuleTeachers();
        row.lblType.setText(classes.getClassType());
        String teachers = Helper.moduleIDExistsInModuleTeacher(moduleTeacherList, classes.getModuleID())
                ? Helper.getSnippet(Helper.getTeachersForSelectedModule(context, moduleID), 35)
                : context.getString(R.string.no_teacher_assigned);

        row.lblTeachers.setText(teachers);
        Module module = db.getSelectedModule(moduleID);
        row.lblClassTitle.setText(module.getModuleName());
        String room = classes.getRoom().isEmpty() ? context.getString(R.string.no_room_assigned) : classes.getRoom();
        row.lblRoom.setText(room);
        row.lblStartTime.setText(Helper.formatTimeShort(classes.getStartTime()));
        row.lblEndTime.setText(Helper.formatTimeShort(classes.getEndTime()));

    }


    private void getCourseworkDetails(Coursework coursework, CourseworkRow row) {
        String priorityLevel = coursework.getPriority();
        Module module = db.getSelectedModule(coursework.getModuleID());
        String moduleDetails = module.getModuleDetails();
        row.title.setText(Helper.getSnippet(WordUtils.capitalizeFully(coursework.getTitle()), 20));

        row.lblModule.setText(moduleDetails);
        row.priority.setText(priorityLevel);
        row.priority.setTextColor(Helper.getPriorityColour(priorityLevel, context));
        row.time.setText(Helper.formatTimeShort(coursework.getDeadlineTime()));

        row.completionStatus.setText(coursework.isCompleted() ? Status.COMPLETED.label : Status.NOT_COMPLETED.label);
        row.completionStatus.setTextColor(coursework.isCompleted() ? context.getColor(R.color.green) : Color.RED);

    }


    private Intent getCourseworkIntent(int id) {
        Intent intent = new Intent(getContext(), EditCourseworkActivity.class);
        intent.putExtra(CourseworkTable.COLUMN_ID, id);
        return intent;
    }

    private Intent getClassesIntent(int id) {
        Intent intent = new Intent(getContext(), EditClassesActivity.class);
        intent.putExtra(ClassTable.COLUMN_ID, id);
        return intent;
    }

    private int getEventIcon(EventType eventType) {
        switch (eventType) {
            case COURSEWORK:
                return R.drawable.ic_coursework;
            case CLASSES:
                return R.drawable.ic_classes;
            default:
                throw new IllegalStateException("Unexpected value: " + eventType);

        }
    }
}


