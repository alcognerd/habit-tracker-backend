package com.alcognerd.habittracker.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private String status;  // "success" or "error"
    private String message; // Informative message
    private T data;         // Optional, only for success

    // Convenience constructor without data
    public ApiResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }
}
