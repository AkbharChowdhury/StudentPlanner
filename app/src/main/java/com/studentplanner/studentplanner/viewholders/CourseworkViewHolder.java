package com.studentplanner.studentplanner.viewholders;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.studentplanner.studentplanner.DatabaseHelper;
import com.studentplanner.studentplanner.R;
import com.studentplanner.studentplanner.databinding.CourseworkRowBinding;
import com.studentplanner.studentplanner.enums.Status;
import com.studentplanner.studentplanner.models.Coursework;
import com.studentplanner.studentplanner.models.Module;
import com.studentplanner.studentplanner.utils.Helper;

import org.apache.commons.text.WordUtils;

import java.time.LocalDate;
import java.util.Locale;


public class CourseworkViewHolder extends RecyclerView.ViewHolder {
    private final Context context;

    private final TextView tvCourseworkID;

    private final TextView tvCourseworkTitle;
    private final TextView tvCourseworkDescription;
    private final TextView tvDeadline;
    private final TextView tvPriority;
    private final TextView tvTimeLeft;
    private final TextView tvCourseworkModule;
    private final TextView tvCourseworkCompleted;

    private final CardView layout;

    public CardView getLayout() {
        return layout;
    }

    public CourseworkViewHolder(@NonNull CourseworkRowBinding binding) {
        super(binding.getRoot());
        tvCourseworkID = binding.tvCourseworkId;
        tvCourseworkTitle = binding.tvCourseworkTitle;
        tvCourseworkDescription = binding.tvCourseworkDesc;
        tvDeadline = binding.tvCourseworkDeadline;
        tvPriority = binding.tvCourseworkPriority;
        tvTimeLeft = binding.tvTimeLeft;
        tvCourseworkModule = binding.tvCourseworkModule;
        tvCourseworkCompleted = binding.tvCourseworkCompleted;
        layout = binding.layout;
        context = binding.getRoot().getContext();
    }


    public void showDetails(Coursework coursework) {
        Module module = DatabaseHelper.getInstance(context).getSelectedModule(coursework.getModuleID());
        LocalDate deadline = LocalDate.parse(coursework.getDeadline());

        tvCourseworkID.setText(String.valueOf(coursework.getCourseworkID()));
        String title = WordUtils.capitalizeFully(coursework.getTitle());

        tvCourseworkTitle.setText(Helper.getSnippet(title));
        tvCourseworkDescription.setText(coursework.getDescription());
        tvDeadline.setText(String.format(Locale.ENGLISH, "%s, %s",
                Helper.formatDate(coursework.getDeadline()),
                Helper.formatTime(coursework.getDeadlineTime())
        ));
        tvPriority.setText(coursework.getPriority());
        tvPriority.setTextColor(Helper.getPriorityColour(coursework.getPriority(), context));
        tvCourseworkModule.setText(String.format("%s %s", module.getModuleCode(), module.getModuleName()));

        tvTimeLeft.setText(Helper.calcDeadlineDate(deadline, coursework.isCompleted()));

        tvCourseworkCompleted.setText(coursework.isCompleted() ? Status.COMPLETED.label : Status.NOT_COMPLETED.label);
        tvCourseworkCompleted.setTextColor(coursework.isCompleted() ? context.getColor(R.color.green) : Color.RED);

        tvTimeLeft.setTextColor(Helper.getPriorityColour(coursework.getPriority(), context));

    }
}