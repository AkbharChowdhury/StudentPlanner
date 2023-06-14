package com.studentplanner.studentplanner.utils;

import android.content.Context;
import android.util.Patterns;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;
import com.studentplanner.studentplanner.DatabaseHelper;
import com.studentplanner.studentplanner.R;
import com.studentplanner.studentplanner.models.Classes;
import com.studentplanner.studentplanner.models.Coursework;
import com.studentplanner.studentplanner.models.Module;
import com.studentplanner.studentplanner.models.Student;
import com.studentplanner.studentplanner.models.Teacher;
import com.studentplanner.studentplanner.tables.StudentTable;
import com.studentplanner.studentplanner.tables.TeacherTable;

import org.apache.commons.text.WordUtils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class Validation {
    private final Context context;
    private DatabaseHelper db;
    private boolean setAdditionalCheck = false;

    public Validation(Context context) {
        this.context = context;

    }

    public Validation(Context context, DatabaseHelper db) {
        this.context = context;
        this.db = db;
    }


    public boolean validateAddModuleForm(Module module) {
        List<Boolean> errors = new ArrayList<>();

        TextInputLayout txtModuleCode = module.getTxtModuleCode();
        errors.add(isEmpty(txtModuleCode, "Module Code"));
        errors.add(isEmpty(module.getTxtModuleName(), "Module Name"));
        errors.add(moduleExists(txtModuleCode));

        return !errors.contains(true);

    }


    private boolean moduleExists(TextInputLayout code) {
        boolean moduleExists = false;
        if (db.moduleCodeExists(Helper.trimStr(code))) {
            code.setError("This module code already exists");
            moduleExists = true;
        }
        return moduleExists;
    }


    public boolean validateEditModuleForm(Module module, String excludedModuleCode) {
        List<Boolean> errors = new ArrayList<>();
        boolean code = isEmpty(module.getTxtModuleCode(), "Module Code");
        boolean name = isEmpty(module.getTxtModuleName(), "Module Name");
        boolean codeExists = moduleCodeExists(module.getTxtModuleCode(), excludedModuleCode);

        errors.add(code);
        errors.add(name);
        errors.add(codeExists);

        return !errors.contains(true);

    }


    public boolean validateSemesterForm(TextInputLayout txtSemester) {
        ArrayList<Boolean> errors = new ArrayList<>();
        errors.add(isEmpty(txtSemester, "Semester Name"));
        return !errors.contains(true);
    }


    public boolean validateAddCourseworkForm(Coursework errorFields) {
        List<Boolean> errors = new ArrayList<>();
        boolean title = isEmpty(errorFields.getTxtTitle(), "Title");
        boolean moduleID = isDropdownEmpty(errorFields.getTxtModuleID(), "Please select a module!", context.getString(R.string.select_module));
        boolean priority = isDropdownEmpty(errorFields.getTxtPriority(), "Please select a Priority!", context.getString(R.string.select_priority));

        boolean isValidDueTime = isValidDueTime(
                errorFields.getTxtDeadline(),
                errorFields.getTxtDeadlineTimeError(),
                errorFields.getTxtDeadlineTime()
        );
        errors.add(title);
        errors.add(moduleID);
        errors.add(priority);
        errors.add(isValidDueTime);
        return !errors.contains(true);

    }
    public boolean isValidDueTime(AutoCompleteTextView txtDeadline, TextInputLayout txtTimeError, AutoCompleteTextView txtDeadlineTime) {

        LocalDate date = LocalDate.parse(Helper.convertFUllDateToYYMMDD(Helper.trimStr(txtDeadline)));
        LocalTime time = LocalTime.parse(Helper.convertFormattedTimeToDBFormat(txtDeadlineTime.getText().toString()));

        LocalDate today = LocalDate.now();
        if (date.isEqual(today) && time.isBefore(LocalTime.now())){
            txtTimeError.setError("this time should be now or a future time!");
            Helper.longToastMessage(context, "time error");
            return true;

        }
        txtTimeError.setError(null);
        return false;
    }



    public boolean validateEditCourseworkForm(Coursework errorFields) {
        List<Boolean> errors = new ArrayList<>();

        errors.add(isEmpty(errorFields.getTxtTitle(), "Title"));

        errors.add(isPastDate(errorFields.getTxtDeadline(), errorFields.getTxtDeadlineError()));

        boolean isValidDueTime = isValidDueTime(
                errorFields.getTxtDeadline(),
                errorFields.getTxtDeadlineTimeError(),
                errorFields.getTxtDeadlineTime()
        );

        errors.add(isValidDueTime);
        return !errors.contains(true);


    }

    public boolean isEmpty(TextInputLayout textField, String fieldName) {
        String text = Helper.trimStr(textField);
        if (text.isEmpty()) {
            setError(textField, getRequiredFieldError(fieldName));
            return true;
        }
        textField.setError(null);
        return false;
    }


    public boolean isPastDate(AutoCompleteTextView txtDate, TextInputLayout txtDateError) {
        if (isPastDate(Helper.convertFUllDateToYYMMDD(Helper.trimStr(txtDate)))) {
            txtDateError.setError("this date should be today or a future date!");
            return true;
        }
        txtDateError.setError(null);
        return false;
    }


    public boolean moduleCodeExists(TextInputLayout txtModuleCode, String excludedCode) {
        if (db.moduleCodeExists(Helper.trimStr(txtModuleCode), excludedCode)) {
            txtModuleCode.setError("This module code already exists");
            return true;
        }

        txtModuleCode.setError(null);
        return false;

    }

    private boolean isValidName(TextInputLayout textField, String column) {
        String name = Helper.trimStr(textField);
        String fieldName = Helper.capitalise(column);

        if (name.isEmpty()) {
            setError(textField, getRequiredFieldError(fieldName));
            return false;
        }

        if (!isValidName(name)) {
            setError(textField, getNameError(fieldName));
            return false;
        }

        textField.setError(null);
        return true;
    }


    public boolean validateRegisterForm(Student student) {
        List<Boolean> errors = new ArrayList<>();
        setAdditionalCheck = true;
        TextInputLayout txtPassword = student.getTxtPassword();
//        TextInputLayout txtPhone = student.getTxtPhone();
        EditText txtPhone = student.getTxtUserPhone();

        errors.add(isValidName(student.getTxtFirstName(), StudentTable.COLUMN_FIRSTNAME));
        errors.add(isValidName(student.getTxtLastName(), StudentTable.COLUMN_LASTNAME));
        errors.add(isValidEmail(student.getTxtEmail()));
        errors.add(isValidPassword(txtPassword));
        errors.add(isPassword8Chars(txtPassword));
        if (!txtPhone.getText().toString().isEmpty()){
            errors.add(isValidPhone(txtPhone));

        }

        return !errors.contains(false);

    }

    private boolean isPassword8Chars(TextInputLayout textField) {

        String password = Helper.trimStr(textField);
        String fieldName = Helper.capitalise(StudentTable.COLUMN_PASSWORD);


        if (password.isEmpty()) return true;
        int PASSWORD_MIN_LENGTH = 8;
        if (!isMinLength(password, PASSWORD_MIN_LENGTH)) {
            setError(textField, getPasswordMinLengthError(fieldName, PASSWORD_MIN_LENGTH));
            return false;
        }

        textField.setError(null);
        return true;
    }
    private String phoneLength(final int length){
        return String.format(Locale.ENGLISH, context.getString(R.string.phone_error), WordUtils.capitalize(StudentTable.COLUMN_PHONE), length);

    }
    private boolean isValidPhone(EditText textField) {

        String phone = textField.getText().toString();
        List<String> phoneErrors = new ArrayList<>();

        if (phone.startsWith("0") && phone.length() < 11){
            phoneErrors.add(phoneLength(11));
        }
        if (!phone.startsWith("0") && phone.length() < 10){
            phoneErrors.add(phoneLength(10));
        }


        if (phoneErrors.size() > 0) {
            textField.setError(null);

            StringBuilder sb = new StringBuilder();
            for (String error : phoneErrors) {
                sb.append(error).append("\n");
            }
            String errorMessages = sb.toString();
            if (!errorMessages.isEmpty()) {
                textField.setError(errorMessages);

            }
            return false;
        }

        textField.setError(null);
        return true;
    }

//    private boolean isValidPhone(TextInputLayout textField) {
//
//        String phone = Helper.trimStr(textField);
//        List<String> phoneErrors = new ArrayList<>();
//
//
//        int PHONE_MIN_LENGTH = 11;
//        if (!isMinLength(phone, PHONE_MIN_LENGTH)) {
//            phoneErrors.add(String.format(Locale.ENGLISH, "Phone number must be %d digits", PHONE_MIN_LENGTH));
//        }
//        if (!phone.startsWith("0")) {
//            phoneErrors.add("Phone number must start with 0");
//
//        }
//        if (phoneErrors.size() > 0) {
//            textField.setError(null);
//
//            StringBuilder sb = new StringBuilder();
//            for (String error : phoneErrors) {
//                sb.append(error).append("\n");
//            }
//            String errorMessages = sb.toString();
//            if (!errorMessages.isEmpty()) {
//                textField.setError(errorMessages);
//
//            }
//            return false;
//        }
//
//        textField.setError(null);
//        return true;
//    }


    private String getPasswordMinLengthError(String fieldName, int minLength) {
        return String.format(context.getString(R.string.password_min_error), fieldName, minLength);
    }

    private boolean isMinLength(String str, int length) {
        return str.length() >= length;
    }


    private boolean isValidEmail(TextInputLayout textField) {

        String email = Helper.trimStr(textField);
        String fieldName = Helper.capitalise(StudentTable.COLUMN_EMAIL);

        if (email.isEmpty()) {
            setError(textField, getRequiredFieldError(fieldName));
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            setError(textField, getInvalidEmailError());
            return false;
        }
        if (setAdditionalCheck) {


            if (db.columnExists(email, StudentTable.COLUMN_EMAIL, StudentTable.TABLE_NAME)) {
                setError(textField, getEmailExistsError());
                return false;
            }
        }

        textField.setError(null);
        return true;
    }


    private boolean isValidEmail(TextInputLayout textField, String excludedEmail) {

        String email = Helper.trimStr(textField);
        String fieldName = Helper.capitalise(StudentTable.COLUMN_EMAIL);

        if (email.isEmpty()) {
            setError(textField, getRequiredFieldError(fieldName));
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            setError(textField, getInvalidEmailError());
            return false;
        }
        if (setAdditionalCheck) {


            if (db.emailExists(email, excludedEmail)) {
                setError(textField, getEmailExistsError());
                return false;
            }
        }

        textField.setError(null);
        return true;
    }
    private boolean isValidTeacherEmail(TextInputLayout textField) {

        String email = Helper.trimStr(textField);
        String fieldName = Helper.capitalise(TeacherTable.COLUMN_EMAIL);

        if (email.isEmpty()) {
            setError(textField, getRequiredFieldError(fieldName));
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            setError(textField, getInvalidEmailError());
            return false;
        }
        if (setAdditionalCheck) {

            if (db.columnExists(email, TeacherTable.COLUMN_EMAIL, TeacherTable.TABLE_NAME)) {
                setError(textField, getEmailExistsError());
                return false;
            }
        }

        textField.setError(null);
        return true;
    }

    private String getRequiredFieldError(String fieldName) {
        return String.format(context.getString(R.string.required_field_error), fieldName);
    }


    private void setError(TextInputLayout textField, String errorMessage) {
        textField.setError(errorMessage);

    }

    private String getNameError(String fieldName) {
        return String.format(context.getString(R.string.required_name_error), fieldName);
    }

    private String getEmailExistsError() {
        return context.getString(R.string.email_exists_error);
    }

    private String getInvalidEmailError() {
        return context.getString(R.string.invalid_email_error);

    }


    private boolean isValidPassword(TextInputLayout textField) {
        String password = Helper.trimStr(textField);
        String fieldName = Helper.capitalise(StudentTable.COLUMN_PASSWORD);

        if (password.isEmpty()) {
            setError(textField, getRequiredFieldError(fieldName));
            return false;
        }

        textField.setError(null);
        return true;
    }

    /**
     * isValidName()
     * ^ beginning of the string
     * [A-Za-z] search for alphabetical chars either they are capitals or not
     * + string contains at least one alphabetical char
     * $ end of the string
     */
    private boolean isValidName(String name) {
        return name.matches("^[A-Za-z]+$");
    }

    public static boolean isPastDate(String date) {
        return LocalDate.parse(date).isBefore(LocalDate.now());
    }


    public boolean validateAddClassForm(Classes errorFields) {

        List<Boolean> errorList = new ArrayList<>();

        boolean dayError = isDropdownEmpty(errorFields.getTxtDayError(), getString(R.string.select_day_error), context.getString(R.string.select_day));
        boolean semesterError = isDropdownEmpty(errorFields.getTxtSemesterError(), getString(R.string.select_semester_error), context.getString(R.string.select_semester));
        boolean moduleError = isDropdownEmpty(errorFields.getTxtModuleError(), getString(R.string.select_module_error), context.getString(R.string.select_module));
        boolean classTypeError = isDropdownEmpty(errorFields.getTxtClassTypeError(), getString(R.string.select_class_type_error), context.getString(R.string.select_class_type));
        boolean startTimeError = isDropdownEmpty(errorFields.getTxtStartTimeError(), getString(R.string.select_start_time_error), context.getString(R.string.select_start_time));
        boolean endTimeError = isDropdownEmpty(errorFields.getTxtEndTimeError(), getString(R.string.select_end_time_error), context.getString(R.string.select_end_time));

        errorList.add(dayError);
        errorList.add(semesterError);
        errorList.add(moduleError);
        errorList.add(classTypeError);
        errorList.add(startTimeError);
        errorList.add(endTimeError);


        return !errorList.contains(true);
    }
    private String getString(int resId){
        return context.getString(resId);
    }

    public static boolean isDropdownEmpty(TextInputLayout textField, String errorMessage, String defaultTextOption) {

        if (textField.getEditText().getText().toString().equals(defaultTextOption)) {
            textField.setError(errorMessage);
            return true;
        }
        textField.setError(null);
        return false;


    }

    // use for edit form fields
    public boolean isDropDownEmpty(TextInputLayout textField, String errorMessage, int selectedID) {
        if (selectedID == 0) {
            textField.setError(errorMessage);
            return true;
        }
        textField.setError(null);
        return false;


    }


    public boolean validateAddTeacherForm(Teacher teacher) {
        List<Boolean> errors = new ArrayList<>();
        setAdditionalCheck = true;

        errors.add(isValidName(teacher.getTxtFirstName(), TeacherTable.COLUMN_FIRSTNAME));
        errors.add(isValidName(teacher.getTxtLastName(), TeacherTable.COLUMN_LASTNAME));
        errors.add(isValidTeacherEmail(teacher.getTxtEmail()));

        return !errors.contains(false);

    }
    public boolean validateEditTeacherForm(Teacher teacher, String excludedEmail) {
        List<Boolean> errors = new ArrayList<>();
        setAdditionalCheck = true;
        errors.add(isValidName(teacher.getTxtFirstName(), TeacherTable.COLUMN_FIRSTNAME));
        errors.add(isValidName(teacher.getTxtLastName(), TeacherTable.COLUMN_LASTNAME));
        errors.add(isValidEmail(teacher.getTxtEmail(), excludedEmail));
        return !errors.contains(false);

    }

}
