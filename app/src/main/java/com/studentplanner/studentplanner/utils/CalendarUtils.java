package com.studentplanner.studentplanner.utils;

import android.content.Context;
import android.content.Intent;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;

import androidx.activity.result.ActivityResultLauncher;

import com.studentplanner.studentplanner.adapters.EventAdapter;
import com.studentplanner.studentplanner.addActivities.AddClassesActivity;
import com.studentplanner.studentplanner.addActivities.AddCourseworkActivity;
import com.studentplanner.studentplanner.models.Event;
import com.studentplanner.studentplanner.tables.ClassTable;
import com.studentplanner.studentplanner.tables.CourseworkTable;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CalendarUtils {

    private static LocalDate selectedDate;

    public static LocalDate getSelectedDate() {
        return selectedDate;
    }

    public static void setSelectedDate(LocalDate selectedDate) {
        CalendarUtils.selectedDate = selectedDate;
    }


    public static String formattedDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
        return date.format(formatter);
    }

    public static String formattedTime(LocalTime time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm:ss a");
        return time.format(formatter);
    }

    public static String monthYearFromDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        return date.format(formatter);
    }

    public static ArrayList<LocalDate> daysInMonthArray(LocalDate date) {
        ArrayList<LocalDate> daysInMonthArray = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(date);

        int daysInMonth = yearMonth.lengthOfMonth();

        LocalDate firstOfMonth = CalendarUtils.selectedDate.withDayOfMonth(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();

        for (int i = 1; i <= 42; i++) {
            if (i <= dayOfWeek || i > daysInMonth + dayOfWeek)
                daysInMonthArray.add(null);
            else
                daysInMonthArray.add(LocalDate.of(selectedDate.getYear(), selectedDate.getMonth(), i - dayOfWeek));
        }
        return daysInMonthArray;
    }

    public static ArrayList<LocalDate> daysInWeekArray(LocalDate selectedDate) {
        ArrayList<LocalDate> days = new ArrayList<>();
        LocalDate current = sundayForDate(selectedDate);
        LocalDate endDate = current.plusWeeks(1);

        while (current.isBefore(endDate)) {
            days.add(current);
            current = current.plusDays(1);
        }
        return days;
    }

    private static LocalDate sundayForDate(LocalDate current) {
        LocalDate oneWeekAgo = current.minusWeeks(1);

        while (current.isAfter(oneWeekAgo)) {
            if (current.getDayOfWeek() == DayOfWeek.SUNDAY)
                return current;

            current = current.minusDays(1);
        }

        return null;
    }


    public static List<String> getDays() {
        List<String> days = new ArrayList<>();
        for (DayOfWeek dow : DayOfWeek.values()) {
            days.add(dow.getDisplayName(TextStyle.FULL, Locale.UK));
            if (dow == DayOfWeek.FRIDAY) break;
        }
        return days;
    }

    public static int getDOWNumber(String day) {
        return DayOfWeek.valueOf(day.toUpperCase()).getValue();
    }

    public static LocalDate getCurrentDate() {
        return LocalDate.now();
    }

    public static void setEventAdapter(ListView eventListView, Context context, ActivityResultLauncher<Intent> startForResult) {
        ArrayList<Event> dailyEvents = Event.eventsForDate(selectedDate);
        EventAdapter eventAdapter = new EventAdapter(context, dailyEvents, startForResult);
        eventListView.setAdapter(eventAdapter);
    }

    public static List<LocalDate> getRecurringEvents(long numOfDays, LocalDate startDate) {
        return Stream.iterate(startDate, date -> date.plusDays(1))
                .limit(numOfDays)
                .collect(Collectors.toList());

    }

    public static void setSelectedDate(DatePickerFragment datepicker, AutoCompleteTextView textField) {
        datepicker.setCustomDate(LocalDate.parse(Helper.convertFUllDateToYYMMDD(textField.getEditableText().toString())));
    }


    public static Intent courseworkIntent(Context context) {
        Intent intent = new Intent(context, AddCourseworkActivity.class);
        intent.putExtra(CourseworkTable.COLUMN_DEADLINE, Helper.formatDate(CalendarUtils.getSelectedDate().toString()));
        return intent;
    }

    public static Intent classIntent(Context context) {
        Intent intent = new Intent(context, AddClassesActivity.class);
        int dow = getSelectedDate().getDayOfWeek().getValue();
        intent.putExtra(ClassTable.COLUMN_DOW, dow);
        return intent;
    }
}
