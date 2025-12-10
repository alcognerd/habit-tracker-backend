package com.alcognerd.habittracker.repository;


import com.alcognerd.habittracker.model.UserDeviceToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDeviceTokenRepository extends JpaRepository<UserDeviceToken, Long> {
    boolean existsByToken(String token);

    @Query("SELECT udt.token FROM UserDeviceToken udt WHERE udt.user.id = :userId")
    List<String> findTokensByUserId(Long userId);
}
