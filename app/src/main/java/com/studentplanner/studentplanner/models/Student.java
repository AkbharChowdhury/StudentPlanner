
package com.studentplanner.studentplanner.models;

import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;

public final class Student extends User {

    private TextInputLayout txtPhone;
    private EditText txtUserPhone;

    public EditText getTxtUserPhone() {
        return txtUserPhone;
    }

    public void setTxtUserPhone(EditText txtUserPhone) {
        this.txtUserPhone = txtUserPhone;
    }

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

    public Student(String firstname, String lastname, String email, String password) {
        super(firstname, lastname, email);
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

