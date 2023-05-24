//package com.studentplanner.studentplanner.models;
//
//import com.studentplanner.studentplanner.DatabaseHelper;
//import com.studentplanner.studentplanner.enums.EventType;
//
//import java.time.LocalDate;
//import java.time.LocalTime;
//import java.util.ArrayList;
//import java.util.List;
//
//public class Event {
//    private int id;
//
//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }
//
//    private EventType eventType;
//    private String name;
//    private LocalDate date;
//    private LocalTime time;
//    private LocalTime startTime;
//    private int dow;
//    private Coursework coursework;
//    private Classes classes;
//
//    public Coursework getCoursework() {
//        return coursework;
//    }
//
//    public void setCoursework(Coursework coursework) {
//        this.coursework = coursework;
//    }
//
//    public Classes getClasses() {
//        return classes;
//    }
//
//    public void setClasses(Classes classes) {
//        this.classes = classes;
//    }
//
//    public int getDow() {
//        return dow;
//    }
//
//    public void setDow(int dow) {
//        this.dow = dow;
//    }
//
//    public LocalTime getStartTime() {
//        return startTime;
//    }
//
//    public void setStartTime(LocalTime startTime) {
//        this.startTime = startTime;
//    }
//
//    public LocalTime getEndTime() {
//        return endTime;
//    }
//
//    public void setEndTime(LocalTime endTime) {
//        this.endTime = endTime;
//    }
//
//    private LocalTime endTime;
//
//
//    private LocalDate semesterStartDate;
//    private LocalDate semesterEndDate;
//
//    public EventType getEventType() {
//        return eventType;
//    }
//
//    public void setEventType(EventType eventType) {
//        this.eventType = eventType;
//    }
//
//    public static ArrayList<Event> getEventsList() {
//        return eventsList;
//    }
//
//
//    private static final ArrayList<Event> eventsList = new ArrayList<>();
//
//
//    public static ArrayList<Event> eventsForDate(LocalDate date) {
//        ArrayList<Event> events = new ArrayList<>();
//
//        for (Event event : eventsList) {
//            if (event.getDate().equals(date))
//                events.add(event);
//        }
//
//        return events;
//    }
//
//    // for coursework entries
//    public Event(LocalDate date, LocalTime time, EventType eventType) {
//        this.date = date;
//        this.time = time;
//        this.eventType = eventType;
//    }
//
//    // recurring events
//    public Event(
//                 LocalDate date,
//                 LocalTime startTime,
//                 LocalTime endTime,
//                 EventType eventType,
//                 LocalDate startDate,
//                 LocalDate endDate,
//                 int dow) {
//        this.date = date;
//        this.startTime = startTime;
//        this.endTime = endTime;
//        this.eventType = eventType;
//        this.semesterStartDate = startDate;
//        this.semesterEndDate = endDate;
//        this.dow = dow;
//
//    }
//
//    public Event(EventType eventType) {
//        this.eventType = eventType;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public LocalDate getDate() {
//        return date;
//    }
//
//    public void setDate(LocalDate date) {
//        this.date = date;
//    }
//
//    public LocalTime getTime() {
//        return time;
//    }
//
//    public void setTime(LocalTime time) {
//        this.time = time;
//    }
//
//
//    public static List<Event> getCourseworkDetails(DatabaseHelper db) {
//        List<Event> courseworkEventList = new ArrayList<>();
//        List<Coursework> courseworkList = db.getCoursework();
//        if (courseworkList.size() > 0) {
//            for (Coursework coursework : courseworkList) {
//                Event courseworkEvent = new Event(
//                        LocalDate.parse(coursework.getDeadline()),
//                        LocalTime.parse(coursework.getDeadlineTime()),
//                        EventType.COURSEWORK);
//
//                courseworkEvent.setId(coursework.getCourseworkID());
//                courseworkEvent.setCoursework(coursework);
//                courseworkEventList.add(courseworkEvent);
//            }
//        }
//        return courseworkEventList;
//    }
//
//
//
//}





package com.studentplanner.studentplanner.models;

import com.studentplanner.studentplanner.DatabaseHelper;
import com.studentplanner.studentplanner.enums.EventType;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Event {


    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private EventType eventType;
    private String name;
    private LocalDate date;
    private LocalTime time;
    private LocalTime startTime;
    private int dow;
    private LocalTime endTime;

    private Coursework coursework;
    private Classes classes;

    public Coursework getCoursework() {
        return coursework;
    }

    public void setCoursework(Coursework coursework) {
        this.coursework = coursework;
    }

    public Classes getClasses() {
        return classes;
    }

    public void setClasses(Classes classes) {
        this.classes = classes;
    }

    public int getDow() {
        return dow;
    }

    public void setDow(int dow) {
        this.dow = dow;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }



    private LocalDate semesterStartDate;
    private LocalDate semesterEndDate;

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public static ArrayList<Event> getEventsList() {
        return eventsList;
    }


    private static final ArrayList<Event> eventsList = new ArrayList<>();


    public static ArrayList<Event> eventsForDate(LocalDate date) {
        ArrayList<Event> events = new ArrayList<>();

        for (Event event : eventsList) {
            if (event.getDate().equals(date))
                events.add(event);
        }

        return events;
    }

    // for coursework entries
    public Event(LocalDate date, LocalTime time, EventType eventType) {
        this.date = date;
        this.time = time;
        this.eventType = eventType;
    }

    // recurring events
    public Event(
            LocalDate date,
            LocalTime startTime,
            LocalTime endTime,
            EventType eventType,
            LocalDate startDate,
            LocalDate endDate,
            int dow) {
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.eventType = eventType;
        this.semesterStartDate = startDate;
        this.semesterEndDate = endDate;
        this.dow = dow;

    }

    public Event(EventType eventType) {
        this.eventType = eventType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }


    public static List<Event> getCourseworkDetails(DatabaseHelper db) {
        List<Event> courseworkEventList = new ArrayList<>();
        List<Coursework> courseworkList = db.getCoursework();
        if (courseworkList.size() > 0) {
            for (Coursework coursework : courseworkList) {
                Event courseworkEvent = new Event(
                        LocalDate.parse(coursework.getDeadline()),
                        LocalTime.parse(coursework.getDeadlineTime()),
                        EventType.COURSEWORK);

                courseworkEvent.setId(coursework.getCourseworkID());
                courseworkEvent.setCoursework(coursework);
                courseworkEventList.add(courseworkEvent);
            }
        }
        return courseworkEventList;
    }



}

