package com.studentplanner.studentplanner.interfaces;

import java.time.LocalDate;

public interface OnItemListener {
    void onItemClick(int position, LocalDate date);
}