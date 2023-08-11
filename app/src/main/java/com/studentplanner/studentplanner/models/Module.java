package com.studentplanner.studentplanner.models;

import android.content.Context;

import com.google.android.material.textfield.TextInputLayout;
import com.studentplanner.studentplanner.DatabaseHelper;
import com.studentplanner.studentplanner.interfaces.Searchable;

import org.apache.commons.text.WordUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Module implements Searchable {
    private int moduleID;
    private String moduleCode;
    private String moduleName;
    private TextInputLayout txtModuleCode;
    private TextInputLayout txtModuleName;


    public Module(TextInputLayout txtModuleCode, TextInputLayout txtModuleName) {
        this.txtModuleCode = txtModuleCode;
        this.txtModuleName = txtModuleName;
    }

    public TextInputLayout getTxtModuleCode() {
        return txtModuleCode;
    }

    public void setTxtModuleCode(TextInputLayout txtModuleCode) {
        this.txtModuleCode = txtModuleCode;
    }

    public void setTxtModuleName(TextInputLayout txtModuleName) {
        this.txtModuleName = txtModuleName;
    }

    public TextInputLayout getTxtModuleName() {
        return txtModuleName;
    }


    public int getModuleID() {
        return moduleID;
    }

    public Module(String moduleCode, String moduleName) {
        this.moduleCode = moduleCode;
        this.moduleName = moduleName;
    }

    public Module(int moduleID, String moduleCode, String moduleName) {
        this.moduleID = moduleID;
        this.moduleCode = moduleCode;
        this.moduleName = moduleName;
    }

    public String getModuleCode() {
        return moduleCode;
    }

    public String getModuleName() {
        return moduleName;
    }

    public static List<String> populateDropdown(List<Module> list) {
        return list.stream().map(Module::getModuleDetails).toList();
    }

    public String getModuleDetails() {
        return String.format(Locale.ENGLISH, "%s %s", moduleCode.toUpperCase(), WordUtils.capitalizeFully(moduleName));
    }

    private static List<Module> defaultModules() {
        List<Module> moduleList = new ArrayList<>();
        moduleList.add(new Module("COMP1424", "Mobile Application Development"));
        moduleList.add(new Module("COMP1429", "Systems Modelling"));
        moduleList.add(new Module("COMP1430", "Systems Design and Development"));
        moduleList.add(new Module("COMP1436", "User Experience Design"));
        moduleList.add(new Module("COMP1611", "Project Management"));
        moduleList.add(new Module("COMP1618", "Software Tools and Techniques"));
        moduleList.add(new Module("COMP1679", "Strategic IT"));
        moduleList.add(new Module("COMP1833", "Software Quality Management"));
        moduleList.add(new Module("COSK1003", "Essential Professional and Academic Skills for Masters Students"));
        moduleList.add(new Module("COMP1252", "MSc Project"));


        return moduleList;
    }

    public static void addDefaultModules(Context context) {
        DatabaseHelper db = DatabaseHelper.getInstance(context);
        defaultModules().forEach(m -> db.addModule(new Module(m.moduleCode, m.getModuleName())));
    }

    @Override
    public String toString() {
        return "Module{" +
                "moduleCode='" + moduleCode + '\'' +
                ", moduleName='" + moduleName + '\'' +
                '}';
    }


    @Override
    public String searchText() {
        return getModuleDetails();
    }
}
