package com.group13.studysync.ui;

import android.graphics.Color;

// Class to map database priority strings to UI color hexadecimal values
public class TaskColorHelper {
    public static int getColorFromPriority(String priority) {
        if (priority == null) return Color.parseColor("#444444");
        switch (priority) {
            case "High":   return Color.parseColor("#E50000"); // High contrast red
            case "Medium": return Color.parseColor("#AAAAAA"); // Bright grey
            default:       return Color.parseColor("#444444"); // Dark grey fallback
        }
    }
}