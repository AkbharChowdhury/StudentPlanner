package com.studentplanner.studentplanner;

import static com.studentplanner.studentplanner.utils.CalendarUtils.daysInMonthArray;
import static com.studentplanner.studentplanner.utils.CalendarUtils.monthYearFromDate;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.studentplanner.studentplanner.adapters.EventAdapter;
import com.studentplanner.studentplanner.models.Event;
import com.studentplanner.studentplanner.tables.CourseworkTable;
import com.studentplanner.studentplanner.utils.CalendarUtils;
import com.studentplanner.studentplanner.utils.Helper;
import com.studentplanner.studentplanner.utils.Validation;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class CalendarFragment extends Fragment implements CalendarAdapter.OnItemListener {
    private View view;
    private Context context;

    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private ListView eventListView;
    private DatabaseHelper db;


    public CalendarFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    private void initFragment(LayoutInflater inflater, ViewGroup container) {
        view = inflater.inflate(R.layout.fragment_calendar, container, false);
        context = getContext();
        getActivity().setTitle(context.getString(R.string.my_calendar));
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        initFragment(inflater, container);


        db = DatabaseHelper.getInstance(context);
        initWidgets();
        view.findViewById(R.id.btNextMonthAction).setOnClickListener(v -> nextMonthAction());
        view.findViewById(R.id.btnPreviousMonthAction).setOnClickListener(v -> previousMonthAction());

        try {
            getEventsFromDB();


        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        resetToCurrentDate();


        ArrayList<Event> dailyEvents = Event.eventsForDate(CalendarUtils.getSelectedDate());
        EventAdapter eventAdapter = new EventAdapter(context, dailyEvents);
        eventListView.setAdapter(eventAdapter);

        return view;
    }

    private void resetToCurrentDate() {
        CalendarUtils.setSelectedDate(CalendarUtils.getCurrentDate());
        setMonthView();
    }

    private void getEventsFromDB() {
        getClassDetails();
        getCourseworkDetails();
    }

    public void previousMonthAction() {
        CalendarUtils.setSelectedDate(CalendarUtils.getSelectedDate().minusMonths(1));
        setMonthView();
    }

    public void nextMonthAction() {
        CalendarUtils.setSelectedDate(CalendarUtils.getSelectedDate().plusMonths(1));
        setMonthView();
    }


    private void getClassDetails() {
        List<Event> classes = Event.getClassesDetails(db);
        for (Event myClass : classes) {
            LocalDate startDate = myClass.getSemesterStartDate();
            LocalDate endDate = myClass.getSemesterEndDate();
            long numOfDays = ChronoUnit.DAYS.between(startDate, endDate);
            for (LocalDate date : CalendarUtils.getRecurringEvents(numOfDays, startDate)) {
                if (date.getDayOfWeek() == DayOfWeek.of(myClass.getDow())) {

                    Event classEvent = new Event(
                            myClass.getName(),
                            date,
                            myClass.getStartTime(),
                            myClass.getEndTime(),
                            myClass.getEventType(),
                            myClass.getSemesterStartDate(),
                            myClass.getSemesterStartDate(),
                            myClass.getDow()
                    );
                    classEvent.setId(myClass.getId());
                    classEvent.setClasses(myClass.getClasses());
                    Event.getEventsList().add(classEvent);
                }
            }
        }


    }

    private void getCourseworkDetails() {
        List<Event> courseworkEvent = Event.getCourseworkDetails(db);
        Event.getEventsList().addAll(courseworkEvent);
    }

    private void initWidgets() {
        calendarRecyclerView = view.findViewById(R.id.calendarRecyclerView);
        monthYearText = view.findViewById(R.id.monthYearTV);
        eventListView = view.findViewById(R.id.eventListView1);

    }

    private void setMonthView() {
        monthYearText.setText(monthYearFromDate(CalendarUtils.getSelectedDate()));
        ArrayList<LocalDate> daysInMonth = daysInMonthArray(CalendarUtils.getSelectedDate());
        calendarRecyclerView.setLayoutManager(new GridLayoutManager(context, 7));
        calendarRecyclerView.setAdapter(new CalendarAdapter(daysInMonth, this));
        CalendarUtils.setEventAdapter(eventListView, context);
    }


    @Override
    public void onItemClick(int position, LocalDate date) {
        CalendarUtils.setSelectedDate(date);
        setMonthView();
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.classes_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.add_coursework_action) {
            if (Validation.isPastDate(CalendarUtils.getSelectedDate().toString())) {
                Helper.goToActivity(getActivity(), AddCourseworkActivity.class);
                return true;
            }
            // set deadline to selected calendar date
            startForResult.launch(courseworkIntent());

        }
        if (id == R.id.add_class_action) {

            Helper.goToActivity(getActivity(), AddClassesActivity.class);
        }
        if (id == R.id.action_week_view) {
            Helper.goToActivity(getActivity(), WeekViewActivity.class);
        }

        return super.onOptionsItemSelected(item);

    }

    private Intent courseworkIntent() {
        Intent intent = new Intent(getActivity(), AddCourseworkActivity.class);
        intent.putExtra(CourseworkTable.COLUMN_DEADLINE, Helper.formatDate(CalendarUtils.getSelectedDate().toString()));
        return intent;
    }

    private ActivityResultLauncher<Intent> startForResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {

            }
    );





}