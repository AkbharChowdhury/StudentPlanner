package com.studentplanner.studentplanner.viewholders;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.studentplanner.studentplanner.DatabaseHelper;
import com.studentplanner.studentplanner.R;
import com.studentplanner.studentplanner.databinding.CourseworkRowBinding;
import com.studentplanner.studentplanner.enums.Status;
import com.studentplanner.studentplanner.models.Coursework;
import com.studentplanner.studentplanner.models.ImageHandler;
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
    private final ImageView tvImage;
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
        tvImage = binding.tvCourseworkImage;
        layout = binding.layout;
        context = binding.getRoot().getContext();
    }


    public void showDetails(Coursework coursework) {
        Module module = DatabaseHelper.getInstance(context).getSelectedModule(coursework.getModuleID());
        LocalDate deadline = LocalDate.parse(coursework.getDeadline());

        tvCourseworkID.setText(String.valueOf(coursework.getCourseworkID()));
        tvCourseworkTitle.setText(Helper.getSnippet(WordUtils.capitalizeFully(coursework.getTitle())));

        showDescription(coursework.getDescription());

        tvDeadline.setText(showDeadlineDetails(coursework));
        tvPriority.setText(coursework.getPriority());
        tvPriority.setTextColor(Helper.getPriorityColour(coursework.getPriority(), context));
        tvCourseworkModule.setText(module.getModuleDetails());
        tvCourseworkCompleted.setText(coursework.isCompleted() ? Status.COMPLETED.label : Status.NOT_COMPLETED.label);
        tvCourseworkCompleted.setTextColor(coursework.isCompleted() ? context.getColor(R.color.green) : Color.RED);

        showTimeLeft(deadline, coursework);
        ImageHandler.showImage(coursework.getByteImage(), tvImage);


    }


    private void showTimeLeft(LocalDate deadline, Coursework coursework) {

        final String timeLeft = Helper.calcDeadlineDate(deadline, coursework.isCompleted());
        if (!timeLeft.isBlank()){
            tvTimeLeft.setText(timeLeft);
            tvTimeLeft.setTextColor(Helper.getPriorityColour(coursework.getPriority(), context));
            return;
        }
        tvTimeLeft.setVisibility(View.GONE);
    }


    private void showDescription(final String description) {
        if (description.length() == 0){
            tvCourseworkDescription.setVisibility(View.GONE);
        } else {
            tvCourseworkDescription.setText(description);
        }
    }

    private String showDeadlineDetails(Coursework coursework){
        return String.format(Locale.ENGLISH, "%s, %s",
                Helper.formatDate(coursework.getDeadline()),
                Helper.formatTime(coursework.getDeadlineTime())
        );

    }
}