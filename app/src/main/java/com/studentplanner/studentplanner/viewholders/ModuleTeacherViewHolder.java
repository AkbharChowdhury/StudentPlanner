package com.studentplanner.studentplanner.viewholders;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.studentplanner.studentplanner.DatabaseHelper;
import com.studentplanner.studentplanner.databinding.ModuleTeacherRowBinding;
import com.studentplanner.studentplanner.models.Module;
import com.studentplanner.studentplanner.models.ModuleTeacher;
import com.studentplanner.studentplanner.utils.Helper;

public class ModuleTeacherViewHolder extends RecyclerView.ViewHolder{
    private final TextView tvModuleId;

    private final TextView tvModule;
    private final TextView tvTeachers;
    private final CardView layout;
    private final DatabaseHelper db;

    public CardView getLayout() {
        return layout;
    }

    public ModuleTeacherViewHolder(@NonNull ModuleTeacherRowBinding binding) {
        super(binding.getRoot());
        tvModuleId = binding.tvModuleIdTeacher;
        tvModule = binding.tvModule;
        tvTeachers = binding.tvModulesTeachers;
        layout = binding.layout;
        db = DatabaseHelper.getInstance(binding.getRoot().getContext());
    }
    public void showDetails(ModuleTeacher model, int position){
        Module module = db.getSelectedModule(model.moduleID());
        String teachers = Helper.getModuleTeachersList(position, db);
        tvModuleId.setText(String.valueOf(model.moduleID()));
        tvModule.setText(module.getModuleDetails());
        tvTeachers.setText(teachers);
    }
}
