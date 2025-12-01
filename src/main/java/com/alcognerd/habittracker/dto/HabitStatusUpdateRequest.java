package com.alcognerd.habittracker.dto;

import com.alcognerd.habittracker.enums.HabitStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HabitStatusUpdateRequest {
    private HabitStatus status;
}
