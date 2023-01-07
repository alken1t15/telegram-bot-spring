package com.example.springdemobot.model.ads;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdsRepository extends JpaRepository<Ads,Long> {

    List<Ads> findAllBy();
}