package com.alcognerd.habittracker.repository;

import com.alcognerd.habittracker.model.HabitHistory;
import jakarta.persistence.criteria.From;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface HabitHistoryRepository extends JpaRepository<HabitHistory,Long> {

    @Query("""
    SELECT h FROM HabitHistory h
    WHERE h.habit.habitId = :habitId
      AND h.createdAt = CURRENT_DATE
""")
    Optional<HabitHistory> findTodayHistoryByHabitId(@Param("habitId") Long habitId);
    @Query(
            value = """
        SELECT TO_CHAR(created_at, 'YYYY-MM-DD') AS day,
               COUNT(*) AS count
        FROM habit_history
        WHERE status = 'COMPLETED'
          AND created_at BETWEEN :from AND :to
          AND user_id = :userId
        GROUP BY day
        ORDER BY day
        """,
            nativeQuery = true
    )
    List<Object[]> getCompletedInRange(@Param("from") LocalDate from, @Param("to") LocalDate to,@Param("userId") Long userId);


    @Query(
            value = """
        SELECT TO_CHAR(created_at, 'YYYY-MM-DD') AS day,
               COUNT(*) AS count
        FROM habit_history
        WHERE status = 'COMPLETED'
          AND EXTRACT(YEAR FROM created_at) = :year
          AND user_id = :userId
        GROUP BY day
        ORDER BY day
        """,
            nativeQuery = true
    )
    List<Object[]> getCompletedByYear(
            @Param("year") int year,
            @Param("userId") Long userId
    );


    // Find today's history for a habit
    Optional<HabitHistory> findByHabit_HabitIdAndCreatedAt(Long habitId, LocalDate date);

    // Find the latest history date for a habit
    @Query("SELECT MAX(h.createdAt) FROM HabitHistory h WHERE h.habit.habitId = :habitId")
    LocalDate findLastHistoryDateForHabit(@Param("habitI d") Long habitId);
    @Query(value = """ 
    SELECT status, COUNT(*)
    FROM habit_history
    WHERE user_id = :userId
    AND created_at = :createdAt
    GROUP BY status""",
    nativeQuery = true)
    List<Object[]> countTodayByStatus(@Param("userId") Long userId,
                                      @Param("createdAt") LocalDate createdAt);


}
