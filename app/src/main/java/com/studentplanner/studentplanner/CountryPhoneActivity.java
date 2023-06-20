package com.studentplanner.studentplanner;

import android.os.Bundle;
import android.os.Message;
import android.text.InputFilter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.hbb20.CountryCodePicker;
import com.studentplanner.studentplanner.databinding.ActivityCountryPhoneBinding;
import com.studentplanner.studentplanner.utils.Helper;

import org.apache.commons.text.WordUtils;

import java.text.MessageFormat;
import java.util.Locale;

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
        setNumber();

        ccp = binding.ccp;
        ccp.setAutoDetectedCountry(true);
        editText = binding.txtMyPhone;
        editText.setOnKeyListener((v, keyCode, event) -> {
            if (editText.getText().toString().startsWith("0")) {
                setEditTextMaxLength(editText, 11);
            } else {
                setEditTextMaxLength(editText, 10);


            }

            return false;
        });
        button = binding.btnSave;
        button.setOnClickListener(v -> getNumber());


    }
    public void setEditTextMaxLength(final EditText editText, int length) {
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(length);
        editText.setFilters(FilterArray);
    }

    private void setNumber() {
        try {
            phoneLabel = binding.getPhoneNumber;

            CountryCodePicker ccp = binding.ccp;
            ccp.setAutoDetectedCountry(true);
            String code = ccp.getSelectedCountryCodeWithPlus();

            phoneLabel.setText(code);
        } catch (Exception e){
            phoneLabel.setText(e.getMessage());


        }
    }

    private void configCCP() {
        ccp = binding.ccp;
        ccp.setAutoDetectedCountry(true);

    }

    private void getNumber() {
        try {
            String phone = editText.getText().toString();

            String code = ccp.getSelectedCountryCodeWithPlus();
            String number = MessageFormat.format("{0}{1}", code, phone);

            phoneLabel.setText(number);

        } catch (Exception ex){
            Helper.longToastMessage(this, "error: " + ex.getMessage());
            Log.d("XXX",  ex.getMessage());

        }

    }
}