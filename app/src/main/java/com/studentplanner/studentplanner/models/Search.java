package com.studentplanner.studentplanner.models;

import com.studentplanner.studentplanner.interfaces.Searchable;

import java.util.List;
import java.util.stream.Collectors;

public final class Search {

    public static List<? extends Searchable> textSearch(List<? extends Searchable> list, String text){

        return list.stream()
                .filter(p -> p.searchText().toLowerCase().trim().contains(text.toLowerCase()))
                .collect(Collectors.toList());
    }







}
