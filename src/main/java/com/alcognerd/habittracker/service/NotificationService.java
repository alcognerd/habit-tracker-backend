package com.alcognerd.habittracker.service;

import com.alcognerd.habittracker.dto.DeviceTokenRequest;
import com.alcognerd.habittracker.enums.HabitStatus;
import com.alcognerd.habittracker.model.User;
import com.alcognerd.habittracker.model.UserDeviceToken;
import com.alcognerd.habittracker.repository.HabitHistoryRepository;
import com.alcognerd.habittracker.repository.UserDeviceTokenRepository;
import com.alcognerd.habittracker.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NotificationService {

    private final UserDeviceTokenRepository userDeviceTokenRepository;
    private final UserRepository userRepository;
    private final HabitHistoryRepository habitHistoryRepository;



    public NotificationService(UserDeviceTokenRepository userDeviceTokenRepository,UserRepository userRepository,HabitHistoryRepository habitHistoryRepository) {
        this.userDeviceTokenRepository = userDeviceTokenRepository;
        this.userRepository = userRepository;
        this.habitHistoryRepository = habitHistoryRepository;
    }

    public void saveDeviceTokenSystem(User user, DeviceTokenRequest dto) {
        if (userDeviceTokenRepository.existsByToken(dto.getDeviceToken())){
            return;
        }

        UserDeviceToken token = new UserDeviceToken(user,
                dto.getDeviceToken(),
                dto.getDeviceType()
        );
        userDeviceTokenRepository.save(token);
    }

    public List<User> getAllUser(){
        List<User> users = userRepository.findAll();
        return  users;
    }
    public Map<HabitStatus, Long> getTodayStatusCount(Long userId) {

        List<Object[]> results = habitHistoryRepository.countTodayByStatus(
                userId, LocalDate.now()
        );

        Map<HabitStatus, Long> map = new HashMap<>();

        for (Object[] row : results) {
            HabitStatus status = HabitStatus.valueOf(row[0].toString());
            Long count = ((Number) row[1]).longValue();
            map.put(status, count);
        }
        return map;
    }

    public List<String> getDeviceSeviceToken(Long user_id){
        return  userDeviceTokenRepository.findTokensByUserId(user_id);
    }
}
