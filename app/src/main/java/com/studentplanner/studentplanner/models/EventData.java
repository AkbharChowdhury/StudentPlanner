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
        Event.getEventsList().addAll(getCourseworkList());
    }

    private List<Event> getCourseworkList() {
        List<Event> courseworkEventList = new ArrayList<>();
        List<Coursework> courseworkList = db.getCoursework();
        if (!courseworkList.isEmpty()) {
            courseworkList.forEach(coursework -> {
                Event courseworkEvent = new Event(
                        coursework.getDeadline(),
                        coursework.getDeadlineTime(),
                        EventType.COURSEWORK);

                courseworkEvent.setId(coursework.getCourseworkID());
                courseworkEvent.setCoursework(coursework);
                courseworkEventList.add(courseworkEvent);

            });


        }
        return courseworkEventList;
    }

    public void getClassDetails() {
        List<Classes> classList = db.getClasses();
        if (!classList.isEmpty()) {
            classList.forEach(myClass -> {
                Semester semester = db.getSelectedSemester(myClass.getSemesterID());
                LocalDate startDate = semester.start();
                LocalDate endDate = semester.end();
                long numOfDays = ChronoUnit.DAYS.between(startDate, endDate);
                Event.getEventsList().addAll(populateClassData(numOfDays, startDate, myClass));
            });


        }

    }


    private List<Event> populateClassData(long numOfDays, LocalDate startDate, Classes myClass) {
        List<Event> classes = new ArrayList<>();
        CalendarUtils.getRecurringEvents(numOfDays, startDate).forEach(date -> {
            if (date.getDayOfWeek() == DayOfWeek.of(myClass.getDow())) {
                Semester semester = db.getSelectedSemester(myClass.getSemesterID());
                Event classEvent = new Event(date,
                        myClass.getStartTime(),
                        myClass.getEndTime(),
                        EventType.CLASSES,
                        semester.start(),
                        semester.end(),
                        myClass.getDow()
                );

                classEvent.setId(myClass.getClassID());
                classEvent.setClasses(myClass);
                classes.add(classEvent);
            }

        });


        return classes;
    }
}
