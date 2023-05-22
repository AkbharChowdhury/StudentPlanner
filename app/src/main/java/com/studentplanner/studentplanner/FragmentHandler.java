package com.studentplanner.studentplanner;

import android.content.Context;
import android.view.MenuItem;

import androidx.fragment.app.Fragment;
public final class FragmentHandler {



    public Fragment getSelectedFragment(MenuItem item){
        Fragment selectedFragment = null;
        int id = item.getItemId();
        if (id == R.id.nav_reminder) selectedFragment = new ReminderFragment();
        if (id == R.id.nav_calendar) selectedFragment = new CalendarFragment();
        if (id == R.id.nav_coursework) selectedFragment = new CourseworkFragment();
        if (id == R.id.nav_module) selectedFragment = new ModuleFragment();
        if (id == R.id.nav_semester) selectedFragment = new SemesterFragment();
        if (id == R.id.nav_teachers) selectedFragment = new TeacherFragment();
        if (id == R.id.nav_module_teacher) selectedFragment = new ModuleTeacherFragment();
        return selectedFragment;
    }

}