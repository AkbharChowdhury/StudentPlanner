package com.studentplanner.studentplanner.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.studentplanner.studentplanner.DatabaseHelper;
import com.studentplanner.studentplanner.databinding.EventRowBinding;
import com.studentplanner.studentplanner.editActivities.EditClassesActivity;
import com.studentplanner.studentplanner.editActivities.EditCourseworkActivity;
import com.studentplanner.studentplanner.R;
import com.studentplanner.studentplanner.enums.EventType;
import com.studentplanner.studentplanner.enums.Status;
import com.studentplanner.studentplanner.models.Classes;
import com.studentplanner.studentplanner.models.Coursework;
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
    private  final ActivityResultLauncher<Intent> startForResult;

    public EventAdapter(@NonNull Context context, List<Event> events, ActivityResultLauncher<Intent> startForResult) {
        super(context, 0, events);
        this.context = context;
        this.startForResult = startForResult;
    }




    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Event event = getItem(position);

        EventRowBinding binding = EventRowBinding.inflate(LayoutInflater.from(context), parent,false);
        binding.getRoot();
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.event_row, parent, false);

        }


        final DatabaseHelper db = DatabaseHelper.getInstance(context);


        convertView.setOnClickListener(v -> {
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
//

        ImageView classesIcon = convertView.findViewById(R.id.event_icon_classes);
        ImageView courseworkIcon = convertView.findViewById(R.id.event_icon_coursework);
//
        int eventIcon = getEventIcon(event.getEventType());
        classesIcon.setImageResource(eventIcon);
        courseworkIcon.setImageResource(eventIcon);
//
//
        TextView txtClassType = convertView.findViewById(R.id.tv_class_type_lecture);
        TextView txtClassTitle = convertView.findViewById(R.id.tv_class_title);
        TextView txtRoom = convertView.findViewById(R.id.tv_room);
        TextView txtStartTime = convertView.findViewById(R.id.tv_start_time);
        TextView txtEndTime = convertView.findViewById(R.id.tv_end_time);
        ConstraintLayout classRow  = convertView.findViewById(R.id.mainLayoutClasses);
//        RelativeLayout CourseworkRow  = (RelativeLayout) convertView.findViewById(R.id.mainLayoutCoursework);

        switch (event.getEventType()) {
            case COURSEWORK:
                classRow.setVisibility(View.GONE);
                TextView title = convertView.findViewById(R.id.tv_cw_title);
                TextView lblModule = convertView.findViewById(R.id.tv_cw_module);
                TextView priority = convertView.findViewById(R.id.tv_cw_priority);
                TextView time = convertView.findViewById(R.id.tv_cw_time);
                TextView completionStatus = convertView.findViewById(R.id.tv_cw_completed);


                Coursework coursework = event.getCoursework();
                String priorityLevel = coursework.getPriority();

                Module module = DatabaseHelper.getInstance(context).getSelectedModule(coursework.getModuleID());
                String moduleDetails = module.getModuleDetails();
                title.setText(Helper.getSnippet(WordUtils.capitalizeFully(coursework.getTitle()) , 20));

                lblModule.setText(moduleDetails);
                priority.setText(priorityLevel);
                priority.setTextColor(Helper.getPriorityColour(priorityLevel, context));
                time.setText(Helper.formatTimeShort(coursework.getDeadlineTime()));

                completionStatus.setText(coursework.isCompleted() ? Status.COMPLETED.label : Status.NOT_COMPLETED.label);
                completionStatus.setTextColor(coursework.isCompleted()? context.getColor(R.color.green) : Color.RED);
                break;
            case CLASSES:

                convertView.findViewById(R.id.mainLayoutCoursework).setVisibility(View.GONE);
                Classes classes = event.getClasses();
                List<ModuleTeacher> moduleTeacherList  = db.getModuleTeachers();
                String teachers = "No teacher assigned";

                if (Helper.moduleIDExistsInModuleTeacher(moduleTeacherList, classes.getModuleID())){
                    teachers = Helper.getModuleTeachersList(position, db);

                }

                txtClassType.setText(teachers);
                Module m = DatabaseHelper.getInstance(context).getSelectedModule(classes.getModuleID());
                txtClassTitle.setText(m.getModuleName());
                String room = classes.getRoom().isEmpty()? "No room assigned" : classes.getRoom();
                txtRoom.setText(room);
                txtStartTime.setText(Helper.formatTimeShort(classes.getStartTime()));
                txtEndTime.setText(Helper.formatTimeShort(classes.getEndTime()));


                break;
        }
        return convertView;
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


