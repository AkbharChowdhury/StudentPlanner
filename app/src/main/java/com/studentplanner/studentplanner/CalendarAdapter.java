package com.studentplanner.studentplanner;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.studentplanner.studentplanner.enums.EventType;
import com.studentplanner.studentplanner.models.Coursework;
import com.studentplanner.studentplanner.models.Event;
import com.studentplanner.studentplanner.utils.CalendarUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

class CalendarAdapter extends RecyclerView.Adapter<CalendarViewHolder> {
    private final ArrayList<LocalDate> days;
    private final OnItemListener onItemListener;


    public CalendarAdapter(ArrayList<LocalDate> days, OnItemListener onItemListener) {
        this.days = days;
        this.onItemListener = onItemListener;
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.calendar_cell, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (days.size() > 15) {
            //month view
            layoutParams.height = (int) (parent.getHeight() * 0.166666666);
        } else {
            // week view
            layoutParams.height = parent.getHeight();

        }
        return new CalendarViewHolder(view, onItemListener, days);

    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {

        Context context = holder.parentView.getContext();
        DatabaseHelper db = DatabaseHelper.getInstance(context);
        // selected date hover
        final LocalDate date = days.get(position);
        if (date == null) {
            holder.dayOfMonth.setText("");

        } else {
            holder.dayOfMonth.setText(String.valueOf(date.getDayOfMonth()));
            if (date.equals(CalendarUtils.getSelectedDate())) {
                holder.parentView.setBackgroundColor(Color.LTGRAY);

            }

            for (Event event : Event.getEventsList()) {

                if (date.equals(event.getDate())) {
                    getEventIcon(event.getEventType(), holder);


                    TextView lblTotalCoursework = (TextView) holder.parentView.findViewById(R.id.lblCourseworkCounter);
                    if (event.getEventType() == EventType.COURSEWORK) {
                        lblTotalCoursework.setVisibility(View.VISIBLE);

                        int total = db.getCourseworkCountByDate(event.getDate());
                        if (total > 1) {
                            // show number of coursework due on calendar date
                            lblTotalCoursework.setText(String.valueOf(total));
                        }

                    }

                }

            }

        }
    }

    private void getEventIcon(EventType eventType, CalendarViewHolder holder) {
        ImageView imgCoursework = holder.parentView.findViewById(R.id.img_cw_icon);
        ImageView imgClasses = holder.parentView.findViewById(R.id.img_classes_icon);
        switch (eventType) {
            case COURSEWORK:
                imgCoursework.setVisibility(View.VISIBLE);
                break;
            case CLASSES:
                imgClasses.setVisibility(View.VISIBLE);
                break;

        }
    }

    @Override
    public int getItemCount() {
        return days.size();
    }

    public interface OnItemListener {
        void onItemClick(int position, LocalDate date);
    }
}
