package com.studentplanner.studentplanner.models;

import com.studentplanner.studentplanner.interfaces.Searchable;
import com.studentplanner.studentplanner.utils.Helper;

import org.apache.commons.text.WordUtils;

import java.time.LocalDate;
import java.util.List;

public record Semester(int semesterID, String name, LocalDate start, LocalDate end) implements Searchable {
    public Semester(String name, LocalDate start, LocalDate end) {
        this(0, name, start, end);
    }

    public static List<String> populateDropdown(List<Semester> list) {
        return list.stream().map(Semester::getDetails).toList();
    }

    private String getDetails() {

        return String.format("%s (%s to %s)",
                WordUtils.capitalizeFully(name),
                Helper.formatDateShort(start.toString()),
                Helper.formatDateShort(end.toString())

        );
    }

    @Override
    public String searchText() {
        return name;
    }
}