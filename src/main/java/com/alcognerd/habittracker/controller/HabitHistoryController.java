package com.alcognerd.habittracker.controller;

import com.alcognerd.habittracker.dto.ApiResponse;
import com.alcognerd.habittracker.exception.NotFoundException;
import com.alcognerd.habittracker.model.User;
import com.alcognerd.habittracker.service.HabitHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/habits/history")
@RequiredArgsConstructor
public class HabitHistoryController {

    private final HabitHistoryService service;

    @GetMapping
    public ResponseEntity<ApiResponse<Map<String, Long>>> getCompletedCounts(@RequestParam String year, Authentication authentication) {

        User user  =(User) authentication.getPrincipal();
        if(user == null){
            throw new NotFoundException("User not found");
        }

        return service.getCompletedCounts(year,user.getId())
                .map(result -> ResponseEntity.ok(
                        new ApiResponse<>("success", "Completed history fetched successfully", result)
                ))
                .orElseGet(() -> ResponseEntity.ok(
                        new ApiResponse<>("error", "No habit history found for the given year")
                ));
    }
}