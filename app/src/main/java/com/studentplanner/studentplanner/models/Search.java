package com.studentplanner.studentplanner.models;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class Search {


    public static List<? extends SearchText> textSearch(List<? extends SearchText> data, String text){

        return data.stream()
                .filter(p -> p.getSearchText().toLowerCase().trim().contains(text.toLowerCase()))
                .collect(Collectors.toList());
    }



    public static <T> List<?> genericSearch(List<T> data, String text){

            if (isType(data, ListType.TEACHER)){
                List<Teacher> list = (List<Teacher>) data;
                return list.stream()
                        .filter(t -> t.getName().toLowerCase().trim().contains(text.toLowerCase()))
                        .collect(Collectors.toList());

            }
        return new ArrayList<>();
    }
    public static  List<Teacher> getFilteredTeacherList(List<Teacher> list,String text){
        return list.stream()
                .filter(t -> t.getName().toLowerCase().trim().contains(text.toLowerCase()))
                .collect(Collectors.toList());

    }


    private static boolean isType(List<?> list, String listType) {

        if(list != null && !list.isEmpty()) {
            String listDataType = list.get(0).getClass().getSimpleName();
            return listDataType.equals(listType);
        }
        return false;
    }
}
