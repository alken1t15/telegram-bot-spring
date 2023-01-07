package com.example.springdemobot.model.ads;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdsImpl implements AdsService {
    @Autowired
    private AdsRepository adsRepository;

    @Override
    public void save(Ads ads) {
        adsRepository.save(ads);
    }

    @Override
    public List<Ads> findAllBy() {
        return adsRepository.findAllBy();
    }
}