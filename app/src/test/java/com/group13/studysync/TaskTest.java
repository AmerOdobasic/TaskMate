package com.group13.studysync;

import com.group13.studysync.data.Task;
import org.junit.Test;
import static org.junit.Assert.*;

public class TaskTest {

    // Test 1: Verify task completion can be toggled
    @Test
    public void testTaskCompletionToggle() {
        // Provide the 5 required arguments: title, description, date, priority, isComplete
        Task task = new Task("Test", "Desc", "2026-03-24", "Low", false);
        task.setComplete(true);
        assertTrue(task.isComplete());
    }

    // Test 2: Verify task title is stored correctly
    @Test
    public void testTaskTitleNotEmpty() {
        Task task = new Task("Math Assignment", "Desc", "2026-03-24", "Medium", false);
        assertNotNull(task.getTitle());
        assertFalse(task.getTitle().isEmpty());
    }

    // Test 3: Verify priority is stored correctly
    @Test
    public void testTaskPriorityValues() {
        Task task = new Task("Test", "Desc", "2026-03-24", "High", false);
        assertEquals("High", task.getPriority());
    }
}