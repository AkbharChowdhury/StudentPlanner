package com.studentplanner.studentplanner.viewholders;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.studentplanner.studentplanner.databinding.TeacherRowBinding;
import com.studentplanner.studentplanner.models.Teacher;

public class TeacherViewHolder extends RecyclerView.ViewHolder {
    private final TextView tvTeacherId;

    private final TextView tvTeacherName;
    private final CardView layout;

    public CardView getLayout() {
        return layout;
    }

    public TeacherViewHolder(@NonNull TeacherRowBinding binding) {
        super(binding.getRoot());
        tvTeacherId = binding.tvTeacherId;
        tvTeacherName = binding.tvTeacherName;
        layout = binding.layout;


    }
    public void showDetails(Teacher teacher){
        tvTeacherId.setText(String.valueOf(teacher.getUserID()));
        tvTeacherName.setText(teacher.getName());
    }
}
