package com.studentplanner.studentplanner.models;

import com.studentplanner.studentplanner.interfaces.Searchable;

import java.util.List;
import java.util.stream.Collectors;

public final class Search {

    public static List<? extends Searchable> textSearch(final List<? extends Searchable> list, final String text){

        return list
                .stream()
//                .filter(p -> text.trim().contains(p.searchText()))
                .filter(p-> p.searchText().trim().contains(text.toLowerCase().trim()))
                .collect(Collectors.toList());
    }



}
