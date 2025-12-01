package com.alcognerd.habittracker.exception;

import com.alcognerd.habittracker.dto.ApiResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 404s
    @ExceptionHandler({
            NotFoundException.class,
            EntityNotFoundException.class
    })
    public ResponseEntity<ApiResponse<Void>> handleNotFound(RuntimeException ex) {
        ApiResponse<Void> body = new ApiResponse<>(HttpStatus.NOT_FOUND.name(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    // 400s - invalid input / bad request
    @ExceptionHandler({
            BadRequestException.class,
            IllegalArgumentException.class
    })
    public ResponseEntity<ApiResponse<Void>> handleBadRequest(RuntimeException ex) {
        ApiResponse<Void> body = new ApiResponse<>(HttpStatus.BAD_REQUEST.name(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    // 409 - conflict
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ApiResponse<Void>> handleConflict(ConflictException ex) {
        ApiResponse<Void> body = new ApiResponse<>(HttpStatus.CONFLICT.name(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    // validation errors from @Valid
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.joining("; "));
        ApiResponse<Void> body = new ApiResponse<>(HttpStatus.BAD_REQUEST.name(), message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    // fallback
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGeneric(Exception ex) {
        ex.printStackTrace(); // replace with logger.error in real project
        ApiResponse<Void> body = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.name(),
                "An unexpected error occurred. Please try again later.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}