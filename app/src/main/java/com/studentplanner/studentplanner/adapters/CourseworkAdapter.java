package com.studentplanner.studentplanner.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.studentplanner.studentplanner.DatabaseHelper;
import com.studentplanner.studentplanner.editActivities.EditCourseworkActivity;
import com.studentplanner.studentplanner.tables.CourseworkTable;
import com.studentplanner.studentplanner.utils.Helper;
import com.studentplanner.studentplanner.R;
import com.studentplanner.studentplanner.models.Coursework;
import com.studentplanner.studentplanner.models.Module;

import org.apache.commons.text.WordUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;


public class CourseworkAdapter extends RecyclerView.Adapter<CourseworkAdapter.ViewHolder> {

    private List<Coursework> courseworkList;
    private final Context context;
    private  final ActivityResultLauncher<Intent> startForResult;
    private final DatabaseHelper db;



    public CourseworkAdapter(List<Coursework> courseworkList, Context context, ActivityResultLauncher<Intent> startForResult) {
        this.courseworkList = courseworkList;
        this.context = context;
        this.startForResult = startForResult;
        db = DatabaseHelper.getInstance(context);
    }

    public void filterList(List<Coursework> filterlist) {
        courseworkList = filterlist;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CourseworkAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.coursework_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseworkAdapter.ViewHolder holder, int position) {
        Coursework coursework = courseworkList.get(position);

        Module module = db.getSelectedModule(coursework.getModuleID());

        holder.tvCourseworkID.setText(String.valueOf(coursework.getCourseworkID()));
        holder.tvCourseworkTitle.setText(WordUtils.capitalizeFully(coursework.getTitle()));
        holder.tvCourseworkDescription.setText(coursework.getDescription());
        holder.tvDeadline.setText(String.format(Locale.ENGLISH,"%s, %s",
                Helper.formatDate(coursework.getDeadline()),
                Helper.formatTime(coursework.getDeadlineTime())
                ));
        holder.tvPriority.setText(coursework.getPriority());
        holder.tvPriority.setTextColor(Helper.getPriorityColour(coursework.getPriority(), context));
        holder.tvCourseworkModule.setText(String.format("%s %s", module.getModuleCode(), module.getModuleName()));
        holder.tvTimeLeft.setText(Helper.calcDeadlineDate(LocalDate.parse(coursework.getDeadline())));


        holder.tvCourseworkCompleted.setText(coursework.isCompleted()? "Completed" : "Not Completed");
        holder.tvCourseworkCompleted.setTextColor(coursework.isCompleted()? context.getColor(R.color.green) : Color.RED);


        holder.layout.setOnClickListener(v -> startForResult.launch(intent(position)));

        holder.tvTimeLeft.setTextColor(Helper.getPriorityColour(coursework.getPriority(), context));

    }

    private Intent intent(int position){
        Intent intent = new Intent(context, EditCourseworkActivity.class);
        intent.putExtra(CourseworkTable.COLUMN_ID, courseworkList.get(position).getCourseworkID());
        return intent;
    }

    @Override
    public int getItemCount() {
        return courseworkList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvCourseworkID;

        private final TextView tvCourseworkTitle;
        private final TextView tvCourseworkDescription;
        private final TextView tvDeadline;
        private final TextView tvPriority;
        private final TextView tvTimeLeft;
        private final TextView  tvCourseworkModule;
        private final TextView  tvCourseworkCompleted;





        private final CardView layout;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCourseworkID = itemView.findViewById(R.id.tv_coursework_id);
            tvCourseworkTitle = itemView.findViewById(R.id.tv_coursework_title);
            tvCourseworkDescription = itemView.findViewById(R.id.tv_coursework_desc);
            tvDeadline = itemView.findViewById(R.id.tv_coursework_deadline);
            tvPriority = itemView.findViewById(R.id.tv_coursework_priority);
            tvTimeLeft = itemView.findViewById(R.id.tv_time_left);
            tvCourseworkModule = itemView.findViewById(R.id.tv_coursework_module);
            tvCourseworkCompleted = itemView.findViewById(R.id.tv_coursework_completed);

            layout = itemView.findViewById(R.id.courseworkSearchLayout);


        }
    }
}