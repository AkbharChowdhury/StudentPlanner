package com.studentplanner.studentplanner.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.studentplanner.studentplanner.databinding.CourseworkRowBinding;
import com.studentplanner.studentplanner.editActivities.EditCourseworkActivity;
import com.studentplanner.studentplanner.models.Coursework;
import com.studentplanner.studentplanner.tables.CourseworkTable;
import com.studentplanner.studentplanner.viewholders.CourseworkViewHolder;

import java.util.List;


public class CourseworkAdapter extends RecyclerView.Adapter<CourseworkViewHolder> {

    private List<Coursework> courseworkList;
    private final Context context;
    private  final ActivityResultLauncher<Intent> startForResult;



    public CourseworkAdapter(List<Coursework> courseworkList, Context context, ActivityResultLauncher<Intent> startForResult) {
        this.courseworkList = courseworkList;
        this.context = context;
        this.startForResult = startForResult;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void filterList(List<Coursework> filteredList) {
        courseworkList = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CourseworkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CourseworkViewHolder(CourseworkRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CourseworkViewHolder holder, int position) {
        Coursework coursework = courseworkList.get(position);
        holder.showDetails(coursework);
        holder.getLayout().setOnClickListener(v -> startForResult.launch(intent(position)));



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

}