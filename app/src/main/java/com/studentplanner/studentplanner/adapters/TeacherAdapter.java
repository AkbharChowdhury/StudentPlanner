package com.studentplanner.studentplanner.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.studentplanner.studentplanner.DatabaseHelper;
import com.studentplanner.studentplanner.editActivities.EditCourseworkActivity;
import com.studentplanner.studentplanner.R;
import com.studentplanner.studentplanner.models.Teacher;
import com.studentplanner.studentplanner.tables.TeacherTable;

import java.util.List;

public class TeacherAdapter extends RecyclerView.Adapter<TeacherAdapter.ViewHolder> {

    private List<Teacher> teacherList;
    private final Context context;
    private final Activity activity;
    private final DatabaseHelper db;


    public TeacherAdapter(List<Teacher> teacherList, Context context, Activity activity) {
        this.teacherList = teacherList;
        this.context = context;
        this.activity = activity;
        db = DatabaseHelper.getInstance(context);
    }

    public void filterList(List<Teacher> filterlist) {
        teacherList = filterlist;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TeacherAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.teacher_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeacherAdapter.ViewHolder holder, int position) {
        Teacher teacher = teacherList.get(position);

        holder.tvTeacherId.setText(String.valueOf(teacher.getUserID()));
        holder.tvTeacherName.setText(teacher.getName());


        holder.layout.setOnClickListener(view -> activity.startActivityForResult(intent(position), 1));

    }

    private Intent intent(int position) {
        Intent intent = new Intent(context, EditCourseworkActivity.class);
        intent.putExtra(TeacherTable.COLUMN_ID, teacherList.get(position).getUserID());
        return intent;
    }

    @Override
    public int getItemCount() {
        return teacherList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvTeacherId;

        private final TextView tvTeacherName;


        private final CardView layout;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTeacherId = itemView.findViewById(R.id.tv_teacher_id);
            tvTeacherName = itemView.findViewById(R.id.tv_teacher_name);

            layout = itemView.findViewById(R.id.teacherSearchLayout);


        }
    }
}