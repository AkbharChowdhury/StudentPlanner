package com.studentplanner.studentplanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupNavDrawer(savedInstanceState);
    }

    private void setupNavDrawer(Bundle savedInstanceState) {
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ReminderFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_reminder);
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

//        if (item.getItemId() == R.id.nav_reminder){
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ReminderFragment()).commit();
//        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}