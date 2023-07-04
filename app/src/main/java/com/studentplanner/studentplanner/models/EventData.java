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
        List<Event> courseworkEvent = Event.getCourseworkDetails(db);
        Event.getEventsList().addAll(courseworkEvent);
    }
    public void getClassDetails() {

        List<Classes> classList = db.getClasses();
        if (!classList.isEmpty()) {
            for (Classes myClass : classList) {
                int semesterID = myClass.getSemesterID();
                Semester semester = db.getSelectedSemester(semesterID);

                LocalDate startDate = semester.getStart();
                LocalDate endDate = semester.getEnd();
                long numOfDays = ChronoUnit.DAYS.between(startDate, endDate);
                Event.getEventsList().addAll(populateClassData(numOfDays, startDate, myClass));

            }
        }


    }


    private List<Event> populateClassData(long numOfDays, LocalDate startDate, Classes myClass) {
        List<Event> classes = new ArrayList<>();

        for (LocalDate date : CalendarUtils.getRecurringEvents(numOfDays, startDate)) {
            if (date.getDayOfWeek() == DayOfWeek.of(myClass.getDow())) {
                int semesterID = myClass.getSemesterID();
                Semester semester = db.getSelectedSemester(semesterID);

                Event classEvent = new Event(
                        date,
                        LocalTime.parse(myClass.getStartTime()),
                        LocalTime.parse(myClass.getEndTime()),
                        EventType.CLASSES,
                        semester.getStart(),
                        semester.getEnd(),
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
