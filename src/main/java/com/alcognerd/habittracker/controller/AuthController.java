package com.alcognerd.habittracker.controller;

import com.alcognerd.habittracker.dto.ApiResponse;
import com.alcognerd.habittracker.model.User;
import com.alcognerd.habittracker.utils.CookieUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<User>> getLoggedInUser(Authentication authentication) {

        if (authentication == null) {
            return ResponseEntity.status(401)
                    .body(new ApiResponse<>("error", "User not logged in"));
        }

        User user = (User) authentication.getPrincipal();

        ApiResponse<User> response = new ApiResponse<>(
                "success",
                "User fetched successfully",
                user
        );

        return ResponseEntity.ok(response);
    }

}

