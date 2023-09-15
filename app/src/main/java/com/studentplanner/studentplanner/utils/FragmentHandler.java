package com.studentplanner.studentplanner.utils;

import android.view.MenuItem;

import androidx.fragment.app.Fragment;

import com.studentplanner.studentplanner.R;
import com.studentplanner.studentplanner.fragments.CalendarFragment;
import com.studentplanner.studentplanner.fragments.CourseworkFragment;
import com.studentplanner.studentplanner.fragments.ModuleFragment;
import com.studentplanner.studentplanner.fragments.ModuleTeacherFragment;
import com.studentplanner.studentplanner.fragments.ReminderFragment;
import com.studentplanner.studentplanner.fragments.SemesterFragment;
import com.studentplanner.studentplanner.fragments.TeacherFragment;

import java.util.Map;

public final class FragmentHandler {
    private static final Map<String, Integer> linkId = Map.of(
            "ReminderFragment", R.id.nav_reminder,
            "SemesterFragment", R.id.nav_semester,
            "TeacherFragment", R.id.nav_teachers,
            "ModuleTeacherFragment", R.id.nav_module_teacher,
            "CalendarFragment", R.id.nav_calendar,
            "CourseworkFragment", R.id.nav_coursework,
            "ModuleFragment", R.id.nav_module
    );


    private static final Map<Integer, Fragment> fragments = Map.of(
            R.id.nav_reminder, new ReminderFragment(),
            R.id.nav_semester, new SemesterFragment(),
            R.id.nav_teachers, new TeacherFragment(),
            R.id.nav_module_teacher, new ModuleTeacherFragment(),
            R.id.nav_calendar, new CalendarFragment(),
            R.id.nav_coursework, new CourseworkFragment(),
            R.id.nav_module, new ModuleFragment()
    );

    private FragmentHandler() {

    }

    public static int activeLink(Fragment selectedFragment) {
        return linkId.get(selectedFragment.getClass().getSimpleName());

    }

    public static Fragment getSelectedFragment(MenuItem item) {
        return fragments.get(item.getItemId());
    }

}