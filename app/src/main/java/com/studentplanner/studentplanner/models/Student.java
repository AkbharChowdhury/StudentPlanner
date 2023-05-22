//package com.studentplanner.studentplanner.models;
//
//import com.google.android.material.textfield.TextInputLayout;
//
//public class Student {
//    private TextInputLayout txtFirstName;
//    private TextInputLayout txLastName;
//    private TextInputLayout txtEmail;
//
//    private TextInputLayout txtPhone;
//
//    private TextInputLayout txtPassword;
//    private int studentID;
//    private String firstname;
//    private String lastname;
//    private String email;
//    private String phone;
//
//
//
//    public String getPhone() {
//        return phone;
//    }
//
//    public Student setPhone(String phone) {
//        this.phone = phone;
//        return this;
//    }
//
//    private String password;
//
//
//    // register form
//    public Student(TextInputLayout txtFirstName, TextInputLayout txLastName, TextInputLayout txtEmail, TextInputLayout txtPassword) {
//        this.txtFirstName = txtFirstName;
//        this.txLastName = txLastName;
//        this.txtEmail = txtEmail;
//        this.txtPassword = txtPassword;
//    }
//    public Student(){
//
//    }
//
//
//    public int getStudentID() {
//        return studentID;
//    }
//
//    public void setStudentID(int studentID) {
//        this.studentID = studentID;
//    }
//
//    public String getFirstname() {
//        return firstname;
//    }
//
//    public Student setFirstname(String firstname) {
//        this.firstname = firstname;
//        return this;
//    }
//
//    public String getLastname() {
//        return lastname;
//    }
//
//    public Student setLastname(String lastname) {
//        this.lastname = lastname;
//        return this;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public Student setEmail(String email) {
//        this.email = email;
//        return this;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public Student setPassword(String password) {
//        this.password = password;
//        return this;
//    }
//
//    public TextInputLayout getTxtFirstName() {
//        return txtFirstName;
//    }
//
//    public TextInputLayout getTxtLastName() {
//        return txLastName;
//    }
//
//    public TextInputLayout getTxtEmail() {
//        return txtEmail;
//    }
//
//    public TextInputLayout getTxtPassword() {
//        return txtPassword;
//    }
//    public TextInputLayout getTxtPhone() {
//        return txtPhone;
//    }
//
//    public Student setTxtPhone(TextInputLayout txtPhone) {
//        this.txtPhone = txtPhone;
//        return this;
//    }
//
//}


package com.studentplanner.studentplanner.models;

import com.google.android.material.textfield.TextInputLayout;

public final class Student extends User {

    private TextInputLayout txtPhone;

    private TextInputLayout txtPassword;

    private String phone;


    private String password;

    public String getPhone() {
        return phone;
    }


    public TextInputLayout getTxtPhone() {
        return txtPhone;
    }

    public void setTxtPhone(TextInputLayout txtPhone) {
        this.txtPhone = txtPhone;
    }

    // register form
    public Student(TextInputLayout txtFirstName, TextInputLayout txLastName, TextInputLayout txtEmail, TextInputLayout txtPassword) {
        super(txtFirstName,txLastName,txtEmail);
        this.txtPassword = txtPassword;
    }
    public Student(){

    }

    public Student(String firstname, String lastname, String email, String phone, String password) {
        super(firstname, lastname, email);
        this.phone = phone;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public Student setPassword(String password) {
        this.password = password;
        return this;
    }

    public TextInputLayout getTxtPassword() {
        return txtPassword;
    }



}

