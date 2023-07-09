package com.studentplanner.studentplanner.viewholders;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.studentplanner.studentplanner.databinding.ModuleRowBinding;
import com.studentplanner.studentplanner.models.Module;

public class ModuleViewHolder extends RecyclerView.ViewHolder {
    private final TextView tvModuleID;
    private final TextView tvModuleCode;
    private final TextView tvModuleName;

    private final CardView layout;


    public ModuleViewHolder(@NonNull ModuleRowBinding binding) {
        super(binding.getRoot());
        tvModuleID = binding.tvModuleId;
        tvModuleName = binding.tvModuleName;
        tvModuleCode = binding.tvModuleCode;
        layout = binding.layout;

    }

    public CardView getLayout() {
        return layout;
    }

    public void showDetails(Module module){

        tvModuleID.setText(String.valueOf(module.getModuleID()));
        tvModuleName.setText(module.getModuleName());
        tvModuleCode.setText(module.getModuleCode());

    }

}
