package com.studentplanner.studentplanner.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.studentplanner.studentplanner.R;

import java.util.regex.Pattern;


public final class PasswordValidator {
    private final Context context;
    private final ProgressBar progressBar;

    public PasswordValidator(Context context, ProgressBar progressBar) {
        this.context = context;
        this.progressBar = progressBar;
    }


    public static boolean is8Chars(String password) {
        return password.length() >= 8;
    }

    public static boolean containsSpecialChar(String password) {
        return Pattern.compile("^(?=.*[-+_!@=/#$%^&*., ?{}]).+$").matcher(password).find();
    }

    public static boolean containsNumber(String password) {
        return password.matches(".*[0-9].*");
    }


    public void getProgressBarStatus(int strength, TextView strengthView) {
        switch (strength) {
            case 1 -> {
                final int COLOUR = context.getColor(R.color.orange);
                progressBar.setProgress(20);
                strengthView.setText(context.getString(R.string.weak));
                strengthView.setTextColor(COLOUR);
                setProgressbarColour(COLOUR);
            }
            case 2 -> {
                final int COLOUR = context.getColor(R.color.dark_blue);

                progressBar.setProgress(40);
                strengthView.setText(context.getString(R.string.average));
                strengthView.setTextColor(COLOUR);
                setProgressbarColour(COLOUR);
            }
            case 3 -> {
                final int COLOUR = context.getColor(R.color.green);

                progressBar.setProgress(60);
                strengthView.setText(context.getString(R.string.good));
                strengthView.setTextColor(COLOUR);
                setProgressbarColour(COLOUR);
            }
            case 4 -> {
                final int COLOUR = context.getColor(R.color.dark_yellow);

                progressBar.setProgress(80);
                strengthView.setText(context.getString(R.string.excellent));
                strengthView.setTextColor(COLOUR);
                setProgressbarColour(COLOUR);
            }
            case 5 -> {
                final int COLOUR = context.getColor(R.color.black);

                progressBar.setProgress(100);
                strengthView.setText(context.getString(R.string.strong));
                strengthView.setTextColor(COLOUR);
                setProgressbarColour(COLOUR);
            }
            default -> {
                progressBar.setProgress(0);
                strengthView.setText("");
            }
        }
    }

    private void setProgressbarColour(final int selectedColour) {
        progressBar.setProgressTintList(ColorStateList.valueOf(selectedColour));


    }
    public static boolean containsUpperCase(final String password){
        return !password.equals(password.toLowerCase());
    }
    public static boolean containsLowerCase(final String password){
        return !password.equals(password.toUpperCase());
    }

}
