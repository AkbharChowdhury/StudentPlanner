package com.studentplanner.studentplanner.models;

import com.studentplanner.studentplanner.DatabaseHelper;
import com.studentplanner.studentplanner.enums.EventType;
import com.studentplanner.studentplanner.utils.CalendarUtils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public final class EventData {
    private final DatabaseHelper db;

    public EventData(DatabaseHelper db) {
        this.db = db;
    }

    public void getCourseworkDetails() {
        Event.getEventsList().addAll(Event.getCourseworkList(db));
    }

    public void getClassDetails() {
        List<Classes> classList = db.getClasses();
        if (!classList.isEmpty()) {
            for (Classes myClass : classList) {
                Semester semester = db.getSelectedSemester(myClass.getSemesterID());
                LocalDate startDate = semester.start();
                LocalDate endDate = semester.end();
                long numOfDays = ChronoUnit.DAYS.between(startDate, endDate);
                Event.getEventsList().addAll(populateClassData(numOfDays, startDate, myClass));

            }
        }

    }


    private List<Event> populateClassData(long numOfDays, LocalDate startDate, Classes myClass) {
        List<Event> classes = new ArrayList<>();

        for (LocalDate date : CalendarUtils.getRecurringEvents(numOfDays, startDate)) {
            if (date.getDayOfWeek() == DayOfWeek.of(myClass.getDow())) {

                Semester semester = db.getSelectedSemester(myClass.getSemesterID());

                Event classEvent = new Event(
                        date,
                        LocalTime.parse(myClass.getStartTime()),
                        LocalTime.parse(myClass.getEndTime()),
                        EventType.CLASSES,
                        semester.start(),
                        semester.end(),
                        myClass.getDow()
                );
                classEvent.setId(myClass.getClassID());
                classEvent.setClasses(myClass);
                classes.add(classEvent);
            }
        }
        return classes;
    }
}
