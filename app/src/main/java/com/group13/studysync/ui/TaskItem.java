package com.group13.studysync.ui;

public class TaskItem {
    public String title;
    public String description;
    public int color;
    public String date; // NEW: The specific date of the task!

    public TaskItem(String title, String description, int color, String date) {
        this.title = title;
        this.description = description;
        this.color = color;
        this.date = date;
    }
}