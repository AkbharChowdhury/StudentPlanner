package com.studentplanner.studentplanner.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.studentplanner.studentplanner.DatabaseHelper;
import com.studentplanner.studentplanner.R;
import com.studentplanner.studentplanner.models.ModuleTeacher;
import com.studentplanner.studentplanner.models.Teacher;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import org.apache.commons.text.WordUtils;

public final class Helper {
    private static final String INTENT_MESSAGE = "message";


    private Helper() {
    }

    public static void goToActivity(Activity currentActivity, Class<? extends Activity> activityPageToOpen) {
        currentActivity.startActivity(new Intent(currentActivity, activityPageToOpen));

    }
        public static void goToFragment(View view, Fragment fragmentToOpen){
        AppCompatActivity activity = (AppCompatActivity) view.getContext();
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragmentToOpen).addToBackStack(null).commit();
    }

    public static void toastMessage(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void longToastMessage(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
    public static void shortToastMessage(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static String capitalise(String word) {
        return word.substring(0, 1).toUpperCase() + word.substring(1);
    }

    public static String trimStr(TextInputLayout textField) {
        return textField.getEditText().getText().toString().trim();

    }

    public static String trimStr(AutoCompleteTextView textField) {
        return textField.getText().toString().trim();

    }

    public static String trimStr(TextInputLayout textField, boolean isTrimmed) {
        return textField.getEditText().getText().toString();

    }


    public static void setRedirectMessage(Activity currentActivity, Class<? extends Activity> activityPageToOpen, String message) {
        Intent intent = new Intent(currentActivity, activityPageToOpen);
        intent.putExtra(INTENT_MESSAGE, message);
        currentActivity.startActivity(intent);
    }




    public static void getIntentMessage(Context context, Bundle extras) {
        if (extras != null && extras.getString(Helper.INTENT_MESSAGE) != null) {
            Helper.longToastMessage(context, extras.getString(Helper.INTENT_MESSAGE));
            extras.clear();
        }

    }


    public static String formatDateShort(String date) {

        return DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).format(LocalDate.parse(date));
    }


    public static String removeFirstAndLastCharacter(String str) {
        return str.substring(1, str.length() - 1);
    }

    public static String convertFUllDateToYYMMDD(String dateStr) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("EEEE, MMM dd, yyyy", Locale.ENGLISH);
            return new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(Objects.requireNonNull(formatter.parse(dateStr)));
        } catch (ParseException e) {
            System.out.println(e.getMessage());

        }
        return null;

    }

    public static long setFutureDate(long startDate, long noOfDaysBetween) {
        return startDate + (1000 * 60 * 60 * 24 * noOfDaysBetween);
    }

    public static long convertLocalDateToLong(LocalDate localDate) {
        ZoneId defaultZoneId = ZoneId.systemDefault();
        Date date = Date.from(localDate.atStartOfDay(defaultZoneId).toInstant());
        return date.getTime();

    }

    public static long getNow() {
        return System.currentTimeMillis() - 1000;
    }

    public static String formatDate(String date) {
        return DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).format(LocalDate.parse(date));
    }

    public static String formatDateShort1(String date) {
        return DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).format(LocalDate.parse(date));
    }


    public static LocalDate formatDate(int year, int month, int day) {
        return LocalDate.of(year, ++month, day);
    }

    public static String formatTime(String time) {
        String timePattern = "hh:mm a";
        LocalTime localTime = LocalTime.parse(time);
        DateTimeFormatter timeColonFormatter = DateTimeFormatter.ofPattern(timePattern);
        return timeColonFormatter.format(localTime);
    }

    public static String formatTimeShort(String time) {
//        String timeColonPattern = "h:mm a";
//        DateTimeFormatter timeColonFormatter = DateTimeFormatter.ofPattern(timeColonPattern);
//        LocalTime colonTime = LocalTime.parse(time);
//        return timeColonFormatter.format(colonTime);
        String timePattern = "hh:mm a";
        LocalTime localTime = LocalTime.parse(time);
        DateTimeFormatter timeColonFormatter = DateTimeFormatter.ofPattern(timePattern);
        return timeColonFormatter.format(localTime);
    }

    //link https://beginnersbook.com/2014/01/how-to-convert-12-hour-time-to-24-hour-date-in-java/
    public static String convertFormattedTimeToDBFormat(String time) {
        DateFormat pattern = new SimpleDateFormat("hh:mm aa", Locale.ENGLISH);
        try {
            return new SimpleDateFormat("HH:mm", Locale.ENGLISH).format(Objects.requireNonNull(pattern.parse(time)));

        } catch (ParseException pe) {
            pe.printStackTrace();
        }
        return null;
    }

    public static int getPriorityColour(String priority, Context c) {
        switch (priority) {
            case "Low":
                return c.getColor(R.color.green);
            case "Medium":
                return c.getColor(R.color.orange);
            default:
                return c.getColor(R.color.red);


        }
    }

    private static ArrayList<Integer> isWeek() {
        ArrayList<Integer> daysWeek = new ArrayList<>();
        for (int i = 0; i < 365; i++) {
            if (i % 7 == 0) {
                daysWeek.add(i);
            }

        }
        return daysWeek;
    }
    public static String calcDeadlineDate(LocalDate deadline, boolean isCompleted) {
        LocalDate today = LocalDate.now();
        Period p = Period.between(today, deadline);
        long weeks = ChronoUnit.WEEKS.between(today, deadline);
        int days = p.getDays();
        int months = p.getMonths();

        if (days == 0) {
            return "Due Today";
        }

        ArrayList<Integer> isWeek = isWeek();

        StringBuilder sb = new StringBuilder("In ");
        if (months == 0 && days == 0 && !isCompleted) {

            return "Overdue";
        }


        if (months == 1 && days == 0 && isCompleted) {

            return sb.append("1 month").toString();
        }

        if (months == 0 && isWeek.contains(days)) {

            return sb.append(weeks == 1 ? "1 week" : weeks + " weeks").toString();
        }

        if (months > 1 && days == 0){
            return sb.append(months).append(" months").toString();

        }
        if (months > 1 && days == 1){
            return sb.append(months).append(" months").append(" and 1 day").toString();

        }
        if (months == 1 && days == 1){
            return sb.append("1 month and 1 day").toString();

        }

        if (months == 0 && days> 0){
            return sb.append(days).append(" days").toString();

        }



        if (days <0 && !isCompleted){
            return "Overdue";
        } else if (days <0){

            return "";
        }

        return sb.append(months).append(" months and ").append(days).append(" days").toString();



    }


    public static String calcDeadlineDate(LocalDate deadline) {
        LocalDate from = LocalDate.now();

        Period period = Period.between(from, deadline);

        StringBuilder s = new StringBuilder("in ");
        if (period.getMonths() > 1) {
            s.append(period.getMonths()).append(" months and ");
        }


        if (period.getDays() >= 1) {
            s.append(period.getDays()).append(" days");
        }
        if (period.getDays() < 1) {
            return "Due Today";
        }
        return s.toString();
    }


    public static String showFormattedDBTime(String time, Context context) {
        LocalTime t = LocalTime.parse(time);
        String selectedTime = String.format(Locale.getDefault(), context.getString(R.string.time_format_database), t.getHour(), t.getMinute());
        return Helper.formatTime(selectedTime);
    }


    public static void getDays(AutoCompleteTextView field, Context context) {
        final List<String> days = CalendarUtils.getDays();
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.list_item, days);
        field.setAdapter(adapter);
    }

    public static void getStringArray(Context context, AutoCompleteTextView field, int array) {
        final List<String> types = Arrays.asList(context.getResources().getStringArray(array));
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.list_item, types);
        field.setAdapter(adapter);
    }

    public static String[] convertArrayListStringToStringArray(List<String> list) {
        return list.toArray(new String[list.size()]);

    }
    public static List<Integer> convertStringArrayToIntArrayList(List<String> numbers){
        return numbers.stream()
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }
    public static String formatList(String str){
        return str.substring(0, str.lastIndexOf(","));
    }
    public static String getModuleTeachersList(int position, DatabaseHelper db){
        StringBuilder teachers = new StringBuilder();

        List<ModuleTeacher> moduleTeacherList  = db.getModuleTeachers();
        if (moduleTeacherList.size() > 0){
            ModuleTeacher model = moduleTeacherList.get(position);

            for (int teacherId : model.getTeacherIDList()){
                Teacher teacher = db.getSelectedTeacher(teacherId);
                teachers.append(teacher.getName()).append(", ");
            }
            return Helper.formatList(teachers.toString());

        }
        return null;


    }

    public static boolean moduleIDExistsInModuleTeacher(List<ModuleTeacher> moduleTeacherList, int moduleID){
        for (ModuleTeacher moduleTeacher: moduleTeacherList){
            if (moduleTeacher.getModuleID() == moduleID){
                return true;
            }
        }
        return false;
    }
    public static String getReminderTitle(){
        LocalDate now = LocalDate.now();
        String year = String.valueOf(now.getYear());
        String month = now.getMonth().toString();
        return WordUtils.capitalizeFully(String.format(Locale.ENGLISH,"coursework %s %s", month, year));

    }



    public static String readStream(InputStream in) {
        StringBuilder sb = new StringBuilder();
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            String nextLine;
            while ((nextLine = reader.readLine()) != null) {
                sb.append(nextLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }


    public static void setMinTimeStatus(BoundTimePickerDialog deadlineTimePicker, LocalDate date){
        LocalDate today = LocalDate.now();
        deadlineTimePicker.setMinTimeToNow(false);

        if (date.isEqual(today)){
            deadlineTimePicker.setMinTimeToNow(true);
        } else if (date.isAfter(today)) {
            deadlineTimePicker.setMinTimeToNow(false);

        }

    }
    public static void deadlineSetup(BoundTimePickerDialog deadlineTimePicker, LocalDate localDate) {

        Helper.setMinTimeStatus(deadlineTimePicker, localDate);
    }


}
