package com.studentplanner.studentplanner.activities;

import static com.studentplanner.studentplanner.utils.CalendarUtils.daysInWeekArray;
import static com.studentplanner.studentplanner.utils.CalendarUtils.monthYearFromDate;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.studentplanner.studentplanner.R;
import com.studentplanner.studentplanner.adapters.CalendarAdapter;
import com.studentplanner.studentplanner.addActivities.AddClassesActivity;
import com.studentplanner.studentplanner.addActivities.AddCourseworkActivity;
import com.studentplanner.studentplanner.databinding.ActivityWeekViewBinding;
import com.studentplanner.studentplanner.interfaces.OnItemListener;
import com.studentplanner.studentplanner.models.Event;
import com.studentplanner.studentplanner.tables.CourseworkTable;
import com.studentplanner.studentplanner.utils.CalendarUtils;
import com.studentplanner.studentplanner.utils.Helper;
import com.studentplanner.studentplanner.utils.Validation;

import java.time.LocalDate;
import java.util.ArrayList;


public class WeekViewActivity extends AppCompatActivity implements OnItemListener {
    private TextView monthYearText;
    private RecyclerView calendarWeekRecyclerView;
    private ListView eventListView;
    private ActivityWeekViewBinding binding;


    private final ActivityResultLauncher<Intent> startForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        Event.getEventsList().clear();
        setWeekView();

    });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getApplication().getResources().getString(R.string.week_view_calendar));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        binding = ActivityWeekViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initWidgets();
        setWeekView();

    }


    private void initWidgets() {

        calendarWeekRecyclerView = binding.calendarRecyclerWeekView;
        monthYearText = binding.monthYearTVWeekView;
        eventListView = binding.eventListView;
    }

    private void setWeekView() {
        monthYearText.setText(monthYearFromDate(CalendarUtils.getSelectedDate()));
        ArrayList<LocalDate> days = daysInWeekArray(CalendarUtils.getSelectedDate());
        calendarWeekRecyclerView.setLayoutManager(new GridLayoutManager(this, 7));
        calendarWeekRecyclerView.setAdapter(new CalendarAdapter(days, this));
        CalendarUtils.setEventAdapter(eventListView, this, startForResult);
    }


    public void previousWeekAction(View view) {
        CalendarUtils.setSelectedDate(CalendarUtils.getSelectedDate().minusWeeks(1));
        setWeekView();
    }

    public void nextWeekAction(View view) {
        CalendarUtils.setSelectedDate(CalendarUtils.getSelectedDate().plusWeeks(1));
        setWeekView();
    }

    @Override
    public void onItemClick(int position, LocalDate date) {
        CalendarUtils.setSelectedDate(date);
        setWeekView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Helper.longToastMessage(this,"called");
        CalendarUtils.setEventAdapter(eventListView, this, startForResult);

    }


    public void todayAction(View view) {
        CalendarUtils.setSelectedDate(CalendarUtils.getCurrentDate());
        setWeekView();

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.classes_menu, menu);
        changeCalendarIcon(menu);

        return true;
    }
    private void changeCalendarIcon(Menu menu){
        MenuItem weekView = menu.findItem(R.id.action_week_view);
        weekView.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_calendar_view));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.add_coursework_action) {
            if (Validation.isPastDate(CalendarUtils.getSelectedDate().toString())) {
                startForResult.launch(new Intent(this, AddCourseworkActivity.class));
                return true;
            }

            // set deadline to selected calendar date
            startForResult.launch(courseworkIntent());

        }

        if (id == R.id.add_class_action) {

            startForResult.launch(new Intent(this, AddClassesActivity.class));

        }


        if (id == android.R.id.home | id == R.id.action_week_view){
            goBack();
        }


        return super.onOptionsItemSelected(item);

    }

    private Intent courseworkIntent() {
        Intent intent = new Intent(this, AddCourseworkActivity.class);
        intent.putExtra(CourseworkTable.COLUMN_DEADLINE, Helper.formatDate(CalendarUtils.getSelectedDate().toString()));
        return intent;
    }

    private void goBack(){
        CalendarUtils.setEventAdapter(eventListView, this, startForResult);
        finish();
    }

}