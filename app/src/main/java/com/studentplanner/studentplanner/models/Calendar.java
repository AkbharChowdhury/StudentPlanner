package com.studentplanner.studentplanner.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Calendar {
    private List<LocalDate> startDates = new ArrayList();
    private List<LocalDate> endDates = new ArrayList();
    private List<Long> longDays = new ArrayList();
    private List<Classes> myClass = new ArrayList();

    public Calendar(List<LocalDate> startDates, List<LocalDate> endDates, List<Long> longDays, List<Classes> myClass) {
        this.startDates = startDates;
        this.endDates = endDates;
        this.longDays = longDays;
        this.myClass = myClass;
    }

}
