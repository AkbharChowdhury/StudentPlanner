package com.studentplanner.studentplanner.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.studentplanner.studentplanner.R;
import com.studentplanner.studentplanner.databinding.EventRowBinding;
import com.studentplanner.studentplanner.editActivities.EditClassesActivity;
import com.studentplanner.studentplanner.editActivities.EditCourseworkActivity;
import com.studentplanner.studentplanner.enums.EventType;
import com.studentplanner.studentplanner.models.ClassRow;
import com.studentplanner.studentplanner.models.CourseworkRow;
import com.studentplanner.studentplanner.models.Event;
import com.studentplanner.studentplanner.tables.ClassTable;
import com.studentplanner.studentplanner.tables.CourseworkTable;

import java.util.List;

public class EventAdapter extends ArrayAdapter<Event> {

    private final Context context;
    private final ActivityResultLauncher<Intent> startForResult;

    public EventAdapter(@NonNull Context context, List<Event> events, ActivityResultLauncher<Intent> startForResult) {
        super(context, 0, events);
        this.context = context;
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

        ImageView classesIcon = binding.icClasses;
        ImageView courseworkIcon = binding.icCoursework;
        int eventIcon = getEventIcon(event.getEventType());
        classesIcon.setImageResource(eventIcon);
        courseworkIcon.setImageResource(eventIcon);

        ConstraintLayout classLayout = binding.mainLayoutClasses;
        ConstraintLayout courseworkLayout = binding.mainLayoutCoursework;

        CourseworkRow courseworkRow = new CourseworkRow(binding, context);
        ClassRow classRow = new ClassRow(binding, context);

        switch (event.getEventType()) {
            case COURSEWORK:
                classLayout.setVisibility(View.GONE);
                courseworkRow.setDetails(event.getCoursework());
                break;
            case CLASSES:
                courseworkLayout.setVisibility(View.GONE);
                classRow.setDetails(event.getClasses());

                break;
        }


        return binding.getRoot();
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


