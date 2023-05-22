package com.studentplanner.studentplanner.utils;

import android.content.Context;
import android.graphics.Color;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.studentplanner.studentplanner.R;


public final class PasswordValidator {
    private Context context;
    private ProgressBar progressBar;

    public PasswordValidator(Context context, ProgressBar progressBar) {
        this.context = context;
        this.progressBar = progressBar;
    }


    public static boolean is8Chars(String password) {
        return password.length() >= 8;
    }

    public static boolean containsSpecialChar(String password) {
//        Link: https://www.geeksforgeeks.org/check-if-a-string-contains-uppercase-lowercase-special-characters-and-numeric-values/
        return Pattern.compile("^(?=.*[-+_!@=/#$%^&*., ?{}]).+$").matcher(password).find();
    }

    public static boolean containsNumber(String password) {
//        Link: https://www.geeksforgeeks.org/check-if-a-string-contains-uppercase-lowercase-special-characters-and-numeric-values/
        return password.matches(".*[0-9].*");
    }


    public void getProgressBarStatus(int strength, TextView strengthView) {
        switch (strength) {
            case 1:
                progressBar.setProgress(20);
                strengthView.setText(context.getString(R.string.weak));
                strengthView.setTextColor(context.getColor(R.color.orange));
                setProgressbarColour(context.getColor(R.color.orange));
                break;
            case 2:
                progressBar.setProgress(40);
                strengthView.setText(context.getString(R.string.average));
                strengthView.setTextColor(context.getColor(R.color.dark_blue));
                setProgressbarColour(context.getColor(R.color.dark_blue));
                break;
            case 3:
                progressBar.setProgress(60);
                strengthView.setText(context.getString(R.string.good));
                strengthView.setTextColor(context.getColor(R.color.green));
                setProgressbarColour(context.getColor(R.color.green));
                break;


            case 4:
                progressBar.setProgress(80);
                strengthView.setText(context.getString(R.string.excellent));
                strengthView.setTextColor(context.getColor(R.color.dark_yellow));
                setProgressbarColour(context.getColor(R.color.dark_yellow));
                break;

            case 5:
                progressBar.setProgress(100);
                strengthView.setText(context.getString(R.string.strong));
                strengthView.setTextColor(context.getColor(R.color.black));
                setProgressbarColour(context.getColor(R.color.black));
                break;
            default:
                progressBar.setProgress(0);
                strengthView.setText("");

        }
    }

    private void setProgressbarColour(String selectedColour) {
        progressBar.getProgressDrawable().setColorFilter(
                Color.parseColor(selectedColour), android.graphics.PorterDuff.Mode.SRC_IN);
    }

    private void setProgressbarColour(int selectedColour) {
        progressBar.getProgressDrawable().setColorFilter(
                selectedColour, android.graphics.PorterDuff.Mode.SRC_IN);
    }
    public static boolean containsUpperCase(String password){
        return !password.equals(password.toLowerCase());
    }
    public static boolean containsLowerCase(String password){
        return !password.equals(password.toUpperCase());
    }

}
