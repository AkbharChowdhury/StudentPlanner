package com.studentplanner.studentplanner;

import java.time.LocalDate;

public interface OnItemListener {
    void onItemClick(int position, LocalDate date);
}