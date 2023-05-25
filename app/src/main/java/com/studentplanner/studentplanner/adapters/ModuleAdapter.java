package com.studentplanner.studentplanner.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.studentplanner.studentplanner.R;
import com.studentplanner.studentplanner.editActivities.EditModuleActivity;
import com.studentplanner.studentplanner.models.Module;
import com.studentplanner.studentplanner.tables.ModuleTable;

import java.util.List;

public class ModuleAdapter extends RecyclerView.Adapter<ModuleAdapter.ViewHolder> {

    private List<Module> moduleList;
    private final Context context;
    private  final ActivityResultLauncher<Intent> startForResult;

    public ModuleAdapter(List<Module> moduleList, Context context, ActivityResultLauncher<Intent> startForResult) {
        this.moduleList = moduleList;
        this.context = context;
        this.startForResult = startForResult;
    }


    public void filterList(List<Module> filterlist) {
        moduleList = filterlist;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ModuleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.module_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ModuleAdapter.ViewHolder holder, int position) {
        Module model = moduleList.get(position);
        holder.tvModuleID.setText(String.valueOf(model.getModuleID()));
        holder.tvModuleName.setText(model.getModuleName());
        holder.tvModuleCode.setText(model.getModuleCode());
        holder.layout.setOnClickListener(v -> startForResult.launch(intent(position)));



    }

    private Intent intent(int position){
        Intent intent = new Intent(context, EditModuleActivity.class);
        intent.putExtra(ModuleTable.COLUMN_ID, moduleList.get(position).getModuleID());
        return intent;
    }

    @Override
    public int getItemCount() {
        return moduleList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvModuleID;

        private final TextView tvModuleCode;
        private final TextView tvModuleName;

        private final CardView layout;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvModuleID = itemView.findViewById(R.id.tv_module_id);
            tvModuleName = itemView.findViewById(R.id.tv_module_name);
            tvModuleCode = itemView.findViewById(R.id.tv_module_code);
            layout = itemView.findViewById(R.id.moduleSearchLayout);

        }
    }
}