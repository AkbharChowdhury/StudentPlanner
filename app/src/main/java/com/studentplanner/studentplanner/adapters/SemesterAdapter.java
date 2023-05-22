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

import com.studentplanner.studentplanner.editActivities.EditSemesterActivity;
import com.studentplanner.studentplanner.R;
import com.studentplanner.studentplanner.models.Semester;
import com.studentplanner.studentplanner.tables.SemesterTable;
import com.studentplanner.studentplanner.utils.Helper;

import org.apache.commons.text.WordUtils;

import java.text.MessageFormat;
import java.util.List;

public class SemesterAdapter extends RecyclerView.Adapter<SemesterAdapter.ViewHolder> {

    private List<Semester> semesterList;
    private final Context context;
    private final Activity activity;


    public SemesterAdapter(List<Semester> semesterList, Context context, Activity activity) {
        this.semesterList = semesterList;
        this.context = context;
        this.activity = activity;
    }

    public void filterList(List<Semester> filterlist) {
        semesterList = filterlist;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SemesterAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // below line is to inflate our layout.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.semester_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SemesterAdapter.ViewHolder holder, int position) {
        Semester model = semesterList.get(position);
        String dateMessage = MessageFormat.format("From {0} to {1}",
                Helper.formatDateShort1(model.getStart().toString()),
                Helper.formatDate(model.getEnd().toString())
        );

        holder.tvSemesterID.setText(String.valueOf(model.getSemesterID()));
        holder.tvSemesterName.setText(WordUtils.capitalizeFully(model.getName()));
        holder.tvDateDescription.setText(dateMessage);
        holder.layout.setOnClickListener(view -> activity.startActivityForResult(intent(position), 1));

    }

    private Intent intent(int position){
        Intent intent = new Intent(context, EditSemesterActivity.class);
        intent.putExtra(SemesterTable.COLUMN_ID, semesterList.get(position).getSemesterID());
        return intent;
    }

    @Override
    public int getItemCount() {
        return semesterList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvSemesterID;

        private final TextView tvSemesterName;
        private final TextView tvDateDescription;

        private final CardView layout;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSemesterID = itemView.findViewById(R.id.tv_semester_id);
            tvSemesterName = itemView.findViewById(R.id.tv_semester_name);
            tvDateDescription = itemView.findViewById(R.id.tv_date_description);
            layout = itemView.findViewById(R.id.semesterLayout);


        }
    }
}