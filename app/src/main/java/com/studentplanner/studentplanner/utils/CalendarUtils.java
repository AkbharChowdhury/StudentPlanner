package com.studentplanner.studentplanner.utils;

import android.content.Context;
import android.content.Intent;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;

import androidx.activity.result.ActivityResultLauncher;

import com.studentplanner.studentplanner.adapters.EventAdapter;
import com.studentplanner.studentplanner.models.Event;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class CalendarUtils {
    private CalendarUtils() {

    }

    private static LocalDate selectedDate;

    public static LocalDate getSelectedDate() {
        return selectedDate;
    }

    public static void setSelectedDate(LocalDate selectedDate) {
        CalendarUtils.selectedDate = selectedDate;
    }

    public static String monthYearFromDate(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern("MMMM yyyy"));
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
        List<Event> dailyEvents = Event.eventsForDate(selectedDate);
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


}
