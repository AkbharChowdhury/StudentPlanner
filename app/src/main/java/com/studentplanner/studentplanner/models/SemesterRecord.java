package com.studentplanner.studentplanner.models;

import com.studentplanner.studentplanner.interfaces.Searchable;
import com.studentplanner.studentplanner.utils.Helper;

import org.apache.commons.text.WordUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public record SemesterRecord(String name, LocalDate start, LocalDate end) implements Searchable {
    public SemesterRecord(int semesterID, String name, LocalDate start, LocalDate end){
        this(name, start, end);


    }
    @Override
    public String searchText() {
        return name;
    }
    public static List<String> populateDropdown(List<SemesterRecord> list){
        if (list.size() > 0)
            return list.stream().map(SemesterRecord::getDetails).toList();
        return new ArrayList<>();

    }
    private static String getDetails(SemesterRecord s){
        return String.format("%s (%s to %s)",
                WordUtils.capitalizeFully(s.name()),
                Helper.formatDateShort(s.start().toString()),
                Helper.formatDateShort(s.end().toString())

        );
    }

}
