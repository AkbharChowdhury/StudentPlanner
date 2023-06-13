package com.studentplanner.studentplanner.models;

import android.util.Log;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class Search {



    public  static <T> List<?> genericSearch(List<T> data, String text){

            if (isType(data, ListType.MODULE)){
                List<Module> list = (List<Module>) data;
                return list.stream()
                        .filter(m -> m.getModuleName().toLowerCase().contains(text.toLowerCase()))
                        .collect(Collectors.toList());

            }

            if (isType(data, ListType.TEACHER)){
                List<Teacher> list = (List<Teacher>) data;
                return list.stream()
                        .filter(t -> t.getName().toLowerCase().trim().contains(text.toLowerCase()))
                        .collect(Collectors.toList());

            }


            if (isType(data, ListType.COURSEWORK)){
                List<Coursework> list = (List<Coursework>) data;
                return list.stream()
                        .filter(c -> c.getTitle().toLowerCase().trim().contains(text.toLowerCase()))
                        .collect(Collectors.toList());
            }



            if (isType(data, ListType.SEMESTER)){
                List<Semester> list = (List<Semester>) data;


                return list.stream()
                        .filter(s -> s.getName().toLowerCase().trim().contains(text.toLowerCase()))
                        .collect(Collectors.toList());
            }


        return new ArrayList<>();
    }


    private static boolean isType(List list, String listType) {

        if(list != null && !list.isEmpty()) {
            String listDataType = list.get(0).getClass().getSimpleName();
            return listDataType.equals(listType);
        }
        return false;
    }
}
