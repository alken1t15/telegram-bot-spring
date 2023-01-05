package com.example.springdemobot.model;

import java.util.List;

public interface UserService {

    void save(User user);

    List<User> findByChatId(Long id);
}