package com.studentplanner.studentplanner.models;

import com.studentplanner.studentplanner.interfaces.Searchable;
import com.studentplanner.studentplanner.utils.Helper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Semester implements Searchable {

    private int semesterID;
    private String name;
    private LocalDate start;
    private LocalDate end;


    public Semester(String name, LocalDate start, LocalDate end) {
        this.name = name;
        this.start = start;
        this.end = end;
    }

    public Semester(int semesterID, String name, LocalDate start, LocalDate end) {
        this.semesterID = semesterID;
        this.name = name;
        this.start = start;
        this.end = end;
    }

    public String getName() {
        return name;
    }

    public LocalDate getStart() {
        return start;
    }

    public LocalDate getEnd() {
        return end;
    }
    public int getSemesterID() {
        return semesterID;
    }


    public static ArrayList<String> populateDropdown(List<Semester> list){
        ArrayList<String> items = new ArrayList<>();
        if (list.size() > 0) {
            for (Semester m : list) {
                items.add(String.format("%s (%s to %s)",
                        m.getName(),
                        Helper.formatDateShort(m.getStart().toString()),
                        Helper.formatDateShort(m.getEnd().toString())

                ));
            }
            return items;
        }
        return items;

    }

    @Override
    public String toString() {
        return "Semester{" +
                "semesterID=" + semesterID +
                ", name='" + name + '\'' +
                ", start=" + start +
                ", end=" + end +
                '}';
    }

    @Override
    public String searchText() {
        return name;
    }
}
