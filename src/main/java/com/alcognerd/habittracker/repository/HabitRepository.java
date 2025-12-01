package com.alcognerd.habittracker.repository;

import com.alcognerd.habittracker.model.Habit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface HabitRepository extends JpaRepository<Habit,Long> {
    List<Habit> findByUserId(Long userId);
    List<Habit> findByUserIdAndEnabledTrue(Long userId);

    Optional<Habit> findByHabitIdAndEnabledTrue(Long habitId);

    Optional<Habit> findByHabitIdAndUserId(Long habitId, Long userId);
}
