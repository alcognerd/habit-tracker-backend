package com.alcognerd.habittracker.exception;

public class HabitNotFoundException extends NotFoundException {
    public HabitNotFoundException(Long habitId) {
        super("Habit not found for id: " + habitId);
    }

    public HabitNotFoundException(String message) {
        super(message);
    }
}
