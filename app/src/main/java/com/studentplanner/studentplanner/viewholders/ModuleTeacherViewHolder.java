package com.studentplanner.studentplanner.viewholders;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.studentplanner.studentplanner.DatabaseHelper;
import com.studentplanner.studentplanner.databinding.ModuleTeacherRowBinding;
import com.studentplanner.studentplanner.models.Module;
import com.studentplanner.studentplanner.models.ModuleTeacher;

import org.apache.commons.text.WordUtils;

import java.util.List;

public class ModuleTeacherViewHolder extends RecyclerView.ViewHolder {
    private final TextView moduleID;
    private final TextView tvModule;
    private final TextView tvTeachers;
    private final CardView layout;
    private final DatabaseHelper db;

    public CardView getLayout() {
        return layout;
    }

    public ModuleTeacherViewHolder(@NonNull ModuleTeacherRowBinding binding) {
        super(binding.getRoot());
        moduleID = binding.tvModuleIdTeacher;
        tvModule = binding.tvModule;
        tvTeachers = binding.tvModulesTeachers;
        layout = binding.layout;
        db = DatabaseHelper.getInstance(binding.getRoot().getContext());
    }

    //    public void showDetails(ModuleTeacher model, int position) {
//        Module module = db.getSelectedModule(model.moduleID());
//        moduleID.setText(String.valueOf(model.moduleID()));
//        tvModule.setText(module.getModuleDetails());
//        tvTeachers.setText(getTeacherNames(position));
//    }
    public void showDetails(List<ModuleTeacher> list, int position) {
//        List<ModuleTeacher> moduleTeacherList
        ModuleTeacher model = list.get(position);
        Module module = db.getSelectedModule(model.moduleID());
        moduleID.setText(String.valueOf(model.moduleID()));
        tvModule.setText(module.getModuleDetails());
        tvTeachers.setText(getTeacherNames(list, position));
    }


    private String getTeacherNames(List<ModuleTeacher> list, int position) {
        StringBuilder sb = new StringBuilder();
//        List<ModuleTeacher> moduleTeacherList1 = db.getModuleTeachers();
        list.get(position)
                .teacherIDList()
                .forEach(id -> sb
                        .append(WordUtils.capitalizeFully(db.getSelectedTeacher(id).getName()))
                        .append(", "));
        return formatList(sb.toString());
    }

    public static String formatList(String str) {
        return str.substring(0, str.lastIndexOf(","));
    }
}
