package com.studentplanner.studentplanner.enums;

public enum Status {
    COMPLETED("Completed"),
    NOT_COMPLETED("Not Completed");

    public final String label;

    Status(String label) {
        this.label = label;
    }
}