package com.studentplanner.studentplanner.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.studentplanner.studentplanner.DatabaseHelper;
import com.studentplanner.studentplanner.EditCourseworkActivity;
import com.studentplanner.studentplanner.EditModuleTeacherActivity;
import com.studentplanner.studentplanner.R;
import com.studentplanner.studentplanner.models.Module;
import com.studentplanner.studentplanner.models.ModuleTeacher;
import com.studentplanner.studentplanner.models.Teacher;
import com.studentplanner.studentplanner.tables.ModuleTable;
import com.studentplanner.studentplanner.tables.ModuleTeacherTable;
import com.studentplanner.studentplanner.tables.TeacherTable;
import com.studentplanner.studentplanner.utils.Helper;

import java.util.List;

public class ModuleTeacherAdapter extends RecyclerView.Adapter<ModuleTeacherAdapter.ViewHolder> {

    private List<ModuleTeacher> moduleTeacherList;
    private final Context context;
    private final Activity activity;
    private final DatabaseHelper db;


    public ModuleTeacherAdapter(List<ModuleTeacher> moduleTeacherList, Context context, Activity activity) {
        this.moduleTeacherList = moduleTeacherList;
        this.context = context;
        this.activity = activity;
        db = DatabaseHelper.getInstance(context);
    }

    public void filterList(List<ModuleTeacher> filterlist) {
        moduleTeacherList = filterlist;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ModuleTeacherAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.module_teacher_row, parent, false);
        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull ModuleTeacherAdapter.ViewHolder holder, int position) {
        ModuleTeacher model = moduleTeacherList.get(position);
        Module module = db.getSelectedModule(model.getModuleID());
        String teachers = Helper.getModuleTeachersList(position, db);

        holder.tvModuleId.setText(String.valueOf(model.getModuleID()));
        holder.tvModule.setText(module.getModuleDetails());
        holder.tvTeachers.setText(teachers);

        holder.layout.setOnClickListener(view -> activity.startActivityForResult(intent(position), 1));

    }

    private Intent intent(int position) {
        Intent intent = new Intent(context, EditModuleTeacherActivity.class);
        intent.putExtra(ModuleTable.COLUMN_ID, moduleTeacherList.get(position).getModuleID());
        return intent;
    }

    @Override
    public int getItemCount() {
        return moduleTeacherList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvModuleId;

        private final TextView tvModule;
        private final TextView tvTeachers;



        private final CardView layout;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvModuleId = itemView.findViewById(R.id.tv_module_id_teacher);
            tvModule = itemView.findViewById(R.id.tv_module);
            tvTeachers = itemView.findViewById(R.id.tv_modules_teachers);


            layout = itemView.findViewById(R.id.moduleTeacherSearchLayout);


        }
    }
}
