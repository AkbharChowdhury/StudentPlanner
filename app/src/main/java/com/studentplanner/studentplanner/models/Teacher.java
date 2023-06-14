package com.studentplanner.studentplanner.models;

import android.content.Context;

import com.google.android.material.textfield.TextInputLayout;
import com.studentplanner.studentplanner.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Teacher extends User {
    public Teacher(String firstname, String lastname, String email) {
        super(firstname, lastname, email);
    }

    public Teacher(TextInputLayout txtFirstName, TextInputLayout txLastName, TextInputLayout txtEmail) {
        super(txtFirstName, txLastName, txtEmail);
    }

    public Teacher(int userID, String firstname, String lastname, String email) {
        super(userID, firstname, lastname, email);
    }

    private static List<Teacher> defaultTeachers(){
        List<Teacher> teacherList = new ArrayList<>();
        teacherList.add(new Teacher("Torrance", "Tumulty","ttumulty7@github.com"));
        teacherList.add(new Teacher("Dallas", "Garlant","dgarlantg@hp.com"));
        teacherList.add(new Teacher("Timmy", "Geck","tgeckk@ning.com"));
        teacherList.add(new Teacher("Milton", "Coppin","mcoppinn@smh.com.au"));
        teacherList.add(new Teacher("Phil", "Persey","pperseym@bluehost.com"));
        return teacherList;
    }
    public static void addDefaultTeachers(Context context){
        DatabaseHelper db = DatabaseHelper.getInstance(context);
        for (Teacher teacher : defaultTeachers()){
            db.addTeacher(new Teacher(teacher.getFirstname(), teacher.getLastname(), teacher.getEmail()));
        }

    }

}
