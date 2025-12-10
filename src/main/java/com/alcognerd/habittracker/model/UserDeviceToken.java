package com.alcognerd.habittracker.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_device_tokens",
        uniqueConstraints = { @UniqueConstraint(columnNames = "token") })
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDeviceToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false,referencedColumnName = "id")
    private User user;

    @Column(nullable = false)
    private String token;

    private String deviceType;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public UserDeviceToken(User user, String deviceToken, String deviceType) {
        this.user = user;
        this.token = deviceToken;
        this.deviceType = deviceType;
        this.createdAt = LocalDateTime.now();
    }

}
