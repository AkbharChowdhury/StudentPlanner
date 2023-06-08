package com.studentplanner.studentplanner;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.hbb20.CountryCodePicker;
import com.studentplanner.studentplanner.databinding.ActivityCountryPhoneBinding;
import com.studentplanner.studentplanner.utils.Helper;

import org.apache.commons.text.WordUtils;

public class CountryPhoneActivity extends AppCompatActivity {
    protected Button button;
    protected TextView phoneLabel;
    protected EditText editText;
    private CountryCodePicker ccp;
    private ActivityCountryPhoneBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_phone);
        setTitle(WordUtils.capitalizeFully("Country calling code"));
        binding = ActivityCountryPhoneBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ccp = binding.ccp;
        editText = binding.editText;
        phoneLabel = binding.getPhoneNumber;
        button = binding.btnSave;
        button.setOnClickListener(v -> getNumber());
//        button.setOnClickListener(v -> Helper.longToastMessage(this, String.valueOf(ccp.getFullNumber())));


    }

    private void getNumber() {
        String phone = editText.getText().toString();
        String ccp = binding.ccp.getFullNumber();
//        String fullNumber  = ccp.getFullNumber() + editText.getText().toString();
        Helper.longToastMessage(getApplicationContext(), ccp);
//        phoneLabel.setText(fullNumber);
    }
}