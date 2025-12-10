package com.alcognerd.habittracker.controller;


import com.alcognerd.habittracker.dto.DeviceTokenRequest;
import com.alcognerd.habittracker.enums.HabitStatus;
import com.alcognerd.habittracker.model.User;
import com.alcognerd.habittracker.service.NotificationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class NotificationController {

    private final NotificationService notificationService;
    @Value("${system.api.key}")
    private  String systemApiKey;
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("/users/device-token")
    public ResponseEntity<?> saveDeviceToken(
            @AuthenticationPrincipal User user,
            @RequestBody DeviceTokenRequest dto
    ) {
        notificationService.saveDeviceTokenSystem(user, dto);
        return ResponseEntity.ok("Token saved");
    }

    @GetMapping("/notification-data")
    public ResponseEntity<?> getTodayHabitStats(@RequestHeader("x-api-key") String apiKey) {


        if (!systemApiKey.equals(apiKey)) {
            return ResponseEntity.status(401).body("Invalid API key");
        }

        // 2️⃣ Fetch habit stats
        List<User> users = notificationService.getAllUser();
        List<Map<String, Object>> responseList = new ArrayList<>();

        for (User user : users) {
            Map<HabitStatus, Long> statusMap = notificationService.getTodayStatusCount(user.getId());
            List<String> tokens = notificationService.getDeviceSeviceToken(user.getId());

            Map<String, Object> userMap = new HashMap<>();
            userMap.put("user_id", user.getId());
            userMap.put("status", statusMap);
            userMap.put("token", tokens);

            responseList.add(userMap);
        }

        return ResponseEntity.ok(responseList);
    }



}
