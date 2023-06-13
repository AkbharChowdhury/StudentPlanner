package com.studentplanner.studentplanner.viewholders;

import android.content.Context;
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
    private final Context context;
    private final TextView tvModuleId;

    private final TextView tvModule;
    private final TextView tvTeachers;
    private final CardView layout;

    public CardView getLayout() {
        return layout;
    }

    public ModuleTeacherViewHolder(@NonNull ModuleTeacherRowBinding binding) {
        super(binding.getRoot());
        tvModuleId = binding.tvModuleIdTeacher;
        tvModule = binding.tvModule;
        tvTeachers = binding.tvModulesTeachers;
        layout = binding.layout;
        context = binding.getRoot().getContext();
    }
    public void showDetails(ModuleTeacher model, int position){
        DatabaseHelper db = DatabaseHelper.getInstance(context);
        Module module = db.getSelectedModule(model.getModuleID());
        String teachers = Helper.getModuleTeachersList(position, db);
        tvModuleId.setText(String.valueOf(model.getModuleID()));
        tvModule.setText(module.getModuleDetails());
        tvTeachers.setText(teachers);
    }
}