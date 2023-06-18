package com.studentplanner.studentplanner.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.studentplanner.studentplanner.databinding.CalendarCellBinding;
import com.studentplanner.studentplanner.viewholders.CalendarViewHolder;
import com.studentplanner.studentplanner.DatabaseHelper;
import com.studentplanner.studentplanner.enums.EventType;
import com.studentplanner.studentplanner.interfaces.OnItemListener;
import com.studentplanner.studentplanner.models.Event;
import com.studentplanner.studentplanner.utils.CalendarUtils;

import java.time.LocalDate;
import java.util.ArrayList;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarViewHolder> {
    private DatabaseHelper db;
    private final ArrayList<LocalDate> days;
    private final OnItemListener onItemListener;
    private CalendarCellBinding binding;
    private ImageView imgCoursework;
    private ImageView imgClasses;

    public CalendarAdapter(ArrayList<LocalDate> days, OnItemListener onItemListener) {
        this.days = days;
        this.onItemListener = onItemListener;
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        binding = CalendarCellBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        db = DatabaseHelper.getInstance(binding.getRoot().getContext());

        imgCoursework = binding.imgCwIcon;
        imgClasses = binding.imgClassesIcon;

        ViewGroup.LayoutParams layoutParams = binding.getRoot().getLayoutParams();
        if (days.size() > 15) {
            //month view
            layoutParams.height = (int) (parent.getHeight() * 0.166666666);
        } else {
            // week view
            layoutParams.height = parent.getHeight();

        }
        return new CalendarViewHolder(binding, onItemListener, days);

    }
    private void showTotalCoursework(final TextView lblTotalCoursework, final LocalDate date){
        final int total = db.getCourseworkCountByDate(date);
        if (total > 1) {
            lblTotalCoursework.setVisibility(View.VISIBLE);
            lblTotalCoursework.setText(String.valueOf(total));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {

        // selected date hover
        final LocalDate date = days.get(position);
        if (date == null) {
            holder.dayOfMonth.setText("");
            return;
        }

        holder.dayOfMonth.setText(String.valueOf(date.getDayOfMonth()));
        if (date.equals(CalendarUtils.getSelectedDate())) {
            holder.parentView.setBackgroundColor(Color.LTGRAY);
        }

        Event.getEventsList().forEach(event -> {
            if (date.equals(event.getDate())) {
                getEventIcon(event.getEventType());
                if (event.getEventType() == EventType.COURSEWORK) {
                    showTotalCoursework(binding.lblCourseworkCounter, event.getDate());
                }

            }

        });

    }

    private void getEventIcon(EventType eventType) {
        switch (eventType) {
            case COURSEWORK -> imgCoursework.setVisibility(View.VISIBLE);
            case CLASSES -> imgClasses.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return days.size();
    }

}
