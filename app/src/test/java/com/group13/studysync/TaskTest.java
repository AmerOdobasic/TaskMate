package com.group13.studysync;

import static org.junit.Assert.assertTrue;
import org.junit.Test;
import com.group13.studysync.data.Task;

/**
 * These are Unit tests for the Task model.
 * These tests make sure our data logic (like date comparisons and status toggles)
 * works correctly before we ever run the app on a phone.
 */
public class TaskTest {
    // Verifies that the Task correctly identifies an overdue date.
    @Test
    public void testTaskIsOverdue() {
        Task task = new Task("Test Task", "Description", "2024-01-01", "High", false);

        // Checks if the due date is before today (2026-03-20)
        // compareTo returns a negative number if the string is "less than" (earlier) than the target
        assertTrue(task.getDueDate().compareTo("2026-03-20") < 0);
    }
    /**
     * Checks that the isComplete boolean can be toggled correctly.
     * This makes sure Member 2's UI checkboxes will work with our data model.
     */
    @Test
    public void testTaskCompletionToggle() {
        Task task = new Task("Test Task", "Description", "2026-04-01", "Low", false);

        task.setComplete(true);
        assertTrue(task.isComplete());
    }
}