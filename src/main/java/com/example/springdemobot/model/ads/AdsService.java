package com.example.springdemobot.model.ads;

import com.example.springdemobot.model.User;

import java.util.List;

public interface AdsService {
    void save(Ads ads);

    List<Ads> findAllBy();
}