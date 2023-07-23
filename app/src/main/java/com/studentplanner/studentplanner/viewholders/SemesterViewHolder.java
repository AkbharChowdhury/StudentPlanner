package com.studentplanner.studentplanner.viewholders;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.studentplanner.studentplanner.databinding.SemesterRowBinding;
import com.studentplanner.studentplanner.models.Semester;
import com.studentplanner.studentplanner.utils.Helper;

import org.apache.commons.text.WordUtils;

import java.text.MessageFormat;

public class SemesterViewHolder  extends RecyclerView.ViewHolder{
    private final TextView semesterID;
    private final TextView name;
    private final TextView description;

    private final CardView layout;
    public SemesterViewHolder(@NonNull SemesterRowBinding binding) {
        super(binding.getRoot());
        semesterID = binding.tvSemesterId;
        name = binding.tvSemesterName;
        description = binding.tvDateDescription;
        layout = binding.layout;
    }

    public CardView getLayout() {
        return layout;
    }

    public void showDetails(Semester semester){
        semesterID.setText(String.valueOf(semester.getSemesterID()));
        name.setText(WordUtils.capitalizeFully(semester.getName()));
        description.setText(getDateMessage(semester));
    }
    private String getDateMessage(Semester semester){
        return MessageFormat.format("From {0} to {1}",
                Helper.formatDateShort(semester.getStart().toString()),
                Helper.formatDate(semester.getEnd().toString())
        );
    }


}
