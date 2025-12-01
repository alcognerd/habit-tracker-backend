package com.alcognerd.habittracker.exception;

public class HabitStreakNotFoundException extends NotFoundException {
    public HabitStreakNotFoundException(Long habitId) {
        super("Habit streak not found for habit id: " + habitId);
    }
}