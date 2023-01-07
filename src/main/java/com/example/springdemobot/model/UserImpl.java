package com.example.springdemobot.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public List<User> findByChatId(Long id) {
        return userRepository.findByChatId(id);
    }

    @Override
    public List<User> findAllBy() {
        return userRepository.findAllBy();
    }


}