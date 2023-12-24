package com.studentplanner.studentplanner.viewholders;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.studentplanner.studentplanner.DatabaseHelper;
import com.studentplanner.studentplanner.databinding.ModuleTeacherRowBinding;
import com.studentplanner.studentplanner.models.ModuleTeacher;

import java.util.List;
import java.util.stream.Collectors;

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

    public void showDetails(List<ModuleTeacher> list, int position) {
        var model = list.get(position);
        moduleID.setText(String.valueOf(model.moduleID()));
        tvModule.setText(db.getSelectedModule(model.moduleID()).getModuleDetails());
        tvTeachers.setText(getTeacherNames(list, position));
    }


    private String getTeacherNames(List<ModuleTeacher> list, int position) {
        return list.get(position)
                .teacherIDList()
                .stream().map(id -> db.getSelectedTeacher(id).getName())
                .collect(Collectors.joining(", "));
    }
}
