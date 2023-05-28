package com.studentplanner.studentplanner.utils;

import android.view.MenuItem;

import androidx.fragment.app.Fragment;

import com.studentplanner.studentplanner.fragments.CalendarFragment;
import com.studentplanner.studentplanner.R;
import com.studentplanner.studentplanner.fragments.CourseworkFragment;
import com.studentplanner.studentplanner.fragments.ModuleFragment;
import com.studentplanner.studentplanner.fragments.ModuleTeacherFragment;
import com.studentplanner.studentplanner.fragments.ReminderFragment;
import com.studentplanner.studentplanner.fragments.SemesterFragment;
import com.studentplanner.studentplanner.fragments.TeacherFragment;

public final class FragmentHandler {



    public Fragment getSelectedFragment(MenuItem item){
        Fragment selectedFragment = null;
        int id = item.getItemId();
        if (id == R.id.nav_reminder) selectedFragment = new ReminderFragment();
        if (id == R.id.nav_semester) selectedFragment = new SemesterFragment();
        if (id == R.id.nav_teachers) selectedFragment = new TeacherFragment();
        if (id == R.id.nav_module_teacher) selectedFragment = new ModuleTeacherFragment();
        if (id == R.id.nav_calendar) selectedFragment = new CalendarFragment();//
        if (id == R.id.nav_coursework) selectedFragment = new CourseworkFragment();
        if (id == R.id.nav_module) selectedFragment = new ModuleFragment();


        return selectedFragment;
    }

}