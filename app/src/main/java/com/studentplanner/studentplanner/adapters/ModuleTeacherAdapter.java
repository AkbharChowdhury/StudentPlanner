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

import com.studentplanner.studentplanner.DatabaseHelper;
import com.studentplanner.studentplanner.databinding.ModuleRowBinding;
import com.studentplanner.studentplanner.databinding.ModuleTeacherRowBinding;
import com.studentplanner.studentplanner.editActivities.EditModuleTeacherActivity;
import com.studentplanner.studentplanner.R;
import com.studentplanner.studentplanner.models.Module;
import com.studentplanner.studentplanner.models.ModuleTeacher;
import com.studentplanner.studentplanner.tables.ModuleTable;
import com.studentplanner.studentplanner.utils.Helper;
import com.studentplanner.studentplanner.viewholders.ModuleTeacherViewHolder;
import com.studentplanner.studentplanner.viewholders.ModuleViewHolder;

import java.util.List;

public class ModuleTeacherAdapter extends RecyclerView.Adapter<ModuleTeacherViewHolder> {

    private List<ModuleTeacher> list;
    private final Context context;
    private  final ActivityResultLauncher<Intent> startForResult;
    private final DatabaseHelper db;


    public ModuleTeacherAdapter(List<ModuleTeacher> list, Context context, ActivityResultLauncher<Intent> startForResult) {
        this.list = list;
        this.context = context;
        this.startForResult = startForResult;
        db = DatabaseHelper.getInstance(context);
    }

    public void filterList(List<ModuleTeacher> filterlist) {
        list = filterlist;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ModuleTeacherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ModuleTeacherViewHolder(ModuleTeacherRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }



    @Override
    public void onBindViewHolder(@NonNull ModuleTeacherViewHolder holder, int position) {
        holder.showDetails(list.get(position), position);
        holder.getLayout().setOnClickListener(v -> startForResult.launch(intent(position)));
    }

    private Intent intent(int position) {
        Intent intent = new Intent(context, EditModuleTeacherActivity.class);
        intent.putExtra(ModuleTable.COLUMN_ID, list.get(position).getModuleID());
        return intent;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
