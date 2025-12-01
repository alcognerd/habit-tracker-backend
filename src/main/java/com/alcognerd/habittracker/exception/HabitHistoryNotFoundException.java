package com.alcognerd.habittracker.exception;

public class HabitHistoryNotFoundException extends NotFoundException {
    public HabitHistoryNotFoundException(Long habitId) {
        super("Habit history not found for habit id: " + habitId + " for today");
    }
}