package com.alcognerd.habittracker.dto;

import lombok.Data;

@Data
public class DeviceTokenRequest {
    private String deviceToken;
    private String deviceType;
}
