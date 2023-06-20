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
    private final TextView tvSemesterID;

    private final TextView tvSemesterName;
    private final TextView tvDateDescription;

    private final CardView layout;
    public SemesterViewHolder(@NonNull SemesterRowBinding binding) {
        super(binding.getRoot());
        tvSemesterID = binding.tvSemesterId;
        tvSemesterName = binding.tvSemesterName;
        tvDateDescription = binding.tvDateDescription;
        layout = binding.layout;


    }

    public CardView getLayout() {
        return layout;
    }

    public void showDetails(Semester semester){
        final String dateMessage = MessageFormat.format("From {0} to {1}",
                Helper.formatDateShort1(semester.getStart().toString()),
                Helper.formatDate(semester.getEnd().toString())
        );

        tvSemesterID.setText(String.valueOf(semester.getSemesterID()));
        tvSemesterName.setText(WordUtils.capitalizeFully(semester.getName()));
        tvDateDescription.setText(dateMessage);
    }


}
