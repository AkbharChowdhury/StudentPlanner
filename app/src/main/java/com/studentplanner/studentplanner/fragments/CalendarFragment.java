package com.studentplanner.studentplanner.fragments;

import static android.app.Activity.RESULT_OK;
import static com.studentplanner.studentplanner.utils.CalendarUtils.daysInMonthArray;
import static com.studentplanner.studentplanner.utils.CalendarUtils.monthYearFromDate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.studentplanner.studentplanner.DatabaseHelper;
import com.studentplanner.studentplanner.R;
import com.studentplanner.studentplanner.activities.WeekViewActivity;
import com.studentplanner.studentplanner.adapters.CalendarAdapter;
import com.studentplanner.studentplanner.adapters.EventAdapter;
import com.studentplanner.studentplanner.addActivities.AddClassesActivity;
import com.studentplanner.studentplanner.addActivities.AddCourseworkActivity;
import com.studentplanner.studentplanner.databinding.FragmentCalendarBinding;
import com.studentplanner.studentplanner.interfaces.OnItemListener;
import com.studentplanner.studentplanner.models.Event;
import com.studentplanner.studentplanner.models.EventData;
import com.studentplanner.studentplanner.tables.CourseworkTable;
import com.studentplanner.studentplanner.utils.CalendarUtils;
import com.studentplanner.studentplanner.utils.Helper;
import com.studentplanner.studentplanner.utils.Validation;

import java.time.LocalDate;
import java.util.ArrayList;

public class CalendarFragment extends Fragment implements OnItemListener {
    private Activity activity;
    private Context context;
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private ListView eventListView;
    private FragmentCalendarBinding binding;
    private EventData eventData;

    private final ActivityResultLauncher<Intent> startForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK) {
            Event.getEventsList().clear();
            getEventsFromDB();
            setMonthView();
        }

    });


    public CalendarFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void initFragment() {
        activity = getActivity();
        context = getContext();
        activity.setTitle(context.getString(R.string.my_calendar));
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        initFragment();
        binding = FragmentCalendarBinding.inflate(inflater, container, false);
        eventData = new EventData(DatabaseHelper.getInstance(context));

        initWidgets();

        binding.btNextMonthAction.setOnClickListener(v -> nextMonthAction());
        binding.btnPreviousMonthAction.setOnClickListener(v -> previousMonthAction());
        binding.btnTodayAction.setOnClickListener(v -> resetToCurrentDate());

        ArrayList<Event> dailyEvents = Event.eventsForDate(CalendarUtils.getSelectedDate());
        EventAdapter eventAdapter = new EventAdapter(context, dailyEvents, startForResult);
        eventListView.setAdapter(eventAdapter);
        getEventsFromDB();
        resetToCurrentDate();
        return binding.getRoot();
    }

    private void resetToCurrentDate() {
        CalendarUtils.setSelectedDate(CalendarUtils.getCurrentDate());
        setMonthView();
    }

    public void getEventsFromDB() {
        eventData.getCourseworkDetails();
        eventData.getClassDetails();
    }

    public void previousMonthAction() {
        CalendarUtils.setSelectedDate(CalendarUtils.getSelectedDate().minusMonths(1));
        setMonthView();
    }

    public void nextMonthAction() {
        CalendarUtils.setSelectedDate(CalendarUtils.getSelectedDate().plusMonths(1));
        setMonthView();
    }


    private void initWidgets() {

        calendarRecyclerView = binding.calendarRecyclerView;
        monthYearText = binding.monthYearTV;
        eventListView = binding.eventListView;

    }

    private void setMonthView() {
        monthYearText.setText(monthYearFromDate(CalendarUtils.getSelectedDate()));
        ArrayList<LocalDate> daysInMonth = daysInMonthArray(CalendarUtils.getSelectedDate());
        calendarRecyclerView.setLayoutManager(new GridLayoutManager(context, 7));
        calendarRecyclerView.setAdapter(new CalendarAdapter(daysInMonth, this));
        CalendarUtils.setEventAdapter(eventListView, context, startForResult);
    }


    @Override
    public void onItemClick(int position, LocalDate date) {
        CalendarUtils.setSelectedDate(date);
        setMonthView();
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        activity.getMenuInflater().inflate(R.menu.classes_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.add_coursework_action) {
            if (Validation.isPastDate(CalendarUtils.getSelectedDate().toString())) {
                openActivity(AddCourseworkActivity.class);
                return true;
            }

            // set deadline to selected calendar date
            startForResult.launch(courseworkIntent());

        }
        if (id == R.id.add_class_action) {
            openActivity(AddClassesActivity.class);
        }

        if (id == R.id.action_week_view) {
            openActivity(WeekViewActivity.class);
        }

        return super.onOptionsItemSelected(item);

    }

    private void openActivity(Class<? extends Activity> activityPageToOpen) {
        startForResult.launch(new Intent(getActivity(), activityPageToOpen));
    }

    private Intent courseworkIntent() {
        Intent intent = new Intent(activity, AddCourseworkActivity.class);
        intent.putExtra(CourseworkTable.COLUMN_DEADLINE, Helper.formatDate(CalendarUtils.getSelectedDate().toString()));
        return intent;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}