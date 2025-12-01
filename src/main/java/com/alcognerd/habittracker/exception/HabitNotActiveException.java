package com.alcognerd.habittracker.exception;

public class HabitNotActiveException extends BadRequestException {
    public HabitNotActiveException(Long habitId) {
        super("Cannot perform operation: habit is not active or is disabled. habitId=" + habitId);
    }
}