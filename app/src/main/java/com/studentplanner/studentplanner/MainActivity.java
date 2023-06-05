package com.studentplanner.studentplanner;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.studentplanner.studentplanner.activities.LoginActivity;
import com.studentplanner.studentplanner.addActivities.AddClassesActivity;
import com.studentplanner.studentplanner.addActivities.AddCourseworkActivity;
import com.studentplanner.studentplanner.fragments.CalendarFragment;
import com.studentplanner.studentplanner.fragments.CourseworkFragment;
import com.studentplanner.studentplanner.fragments.ReminderFragment;
import com.studentplanner.studentplanner.models.Coursework;
import com.studentplanner.studentplanner.models.Student;
import com.studentplanner.studentplanner.utils.AccountPreferences;
import com.studentplanner.studentplanner.utils.FragmentHandler;
import com.studentplanner.studentplanner.utils.Helper;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private DatabaseHelper db;
    private int studentID;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getSupportFragmentManager().popBackStack();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = DatabaseHelper.getInstance(this);
        studentID = AccountPreferences.getStudentID(this);
        isLoggedIn();
        setupNavDrawer(savedInstanceState);
        showStudentDetails();






    }

    private void isLoggedIn() {
        if (db.getStudentEmail(studentID).isEmpty()) {
            Helper.goToActivity(this, LoginActivity.class);
        }
    }


    private void showStudentDetails() {
        Student student = db.getUserFirstAndLastName(studentID);
        // show name
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        TextView user_dashboard = (TextView) header.findViewById(R.id.nav_username_label);
        user_dashboard.setText(student.getName());
        // show email
        String email = db.getStudentEmail(studentID);
        TextView lblEmail = (TextView) header.findViewById(R.id.nav_email_label);
        lblEmail.setText(email);
    }

    private void setupNavDrawer(Bundle savedInstanceState) {
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new CourseworkFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_coursework);

        }

    }

    private void logout() {
        AccountPreferences.logout(this);
        Helper.goToActivity(this, LoginActivity.class);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.nav_logout) logout();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentHandler().getSelectedFragment(item)).commit();

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


}