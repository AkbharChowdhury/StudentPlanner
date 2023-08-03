package com.studentplanner.studentplanner.models;

import android.content.Context;

import com.google.android.material.textfield.TextInputLayout;
import com.studentplanner.studentplanner.DatabaseHelper;
import com.studentplanner.studentplanner.interfaces.Searchable;

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

    public static List<String> populateDropdown(List<Module> list){
        if (list.size() > 0)
            return list.stream().map(Module::getModuleDetails).toList();
        return new ArrayList<>();
    }
    public String getModuleDetails(){
        return String.format(Locale.ENGLISH, "%s %s", moduleCode, moduleName);
    }

    private static List<Module> defaultModules(){
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
    public static void addDefaultModules(Context context){
        DatabaseHelper db = DatabaseHelper.getInstance(context);
        for (Module module : defaultModules()){
            db.addModule(new Module(module.moduleCode, module.getModuleName()));
        }

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
