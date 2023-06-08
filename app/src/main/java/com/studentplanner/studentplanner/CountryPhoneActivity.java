package com.studentplanner.studentplanner;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import org.apache.commons.text.WordUtils;

public class CountryPhoneActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_phone);
        setTitle(WordUtils.capitalizeFully("Country calling code"));
    }
}