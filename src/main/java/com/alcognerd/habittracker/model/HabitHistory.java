package com.alcognerd.habittracker.model;

import com.alcognerd.habittracker.enums.HabitStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "habit_history")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HabitHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "habit_history_id")
    private Long habitHistoryId;

    private Long userId;

    private LocalDate createdAt = LocalDate.now();

    public HabitHistory(Habit habit) {
        this.habit = habit;
    }

    @Enumerated(EnumType.STRING)
    private HabitStatus status = HabitStatus.PENDING;

    @ManyToOne
    @JoinColumn(name = "habit_id", referencedColumnName = "habit_id", nullable = false)
    private Habit habit;

    public HabitHistory(Habit habit, Long userId) {
        this.habit = habit;
        this.userId = userId;
    }
}
