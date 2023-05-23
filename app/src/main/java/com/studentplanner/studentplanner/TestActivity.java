package com.studentplanner.studentplanner;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import com.studentplanner.studentplanner.addActivities.AddModuleActivity;
import com.studentplanner.studentplanner.utils.Helper;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Button button =  findViewById(R.id.buttonTest);
        button.setOnClickListener(v -> Helper.goToActivity(this, AddModuleActivity.class));
        button.setText("Hello World!");

    }
}