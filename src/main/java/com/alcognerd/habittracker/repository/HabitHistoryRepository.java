package com.alcognerd.habittracker.repository;

import com.alcognerd.habittracker.model.HabitHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HabitHistoryRepository extends JpaRepository<HabitHistory,Long> {

    @Query("""
    SELECT h FROM HabitHistory h
    WHERE h.habit.habitId = :habitId
      AND h.createdAt = CURRENT_DATE
""")
    Optional<HabitHistory> findTodayHistoryByHabitId(@Param("habitId") Long habitId);

}
