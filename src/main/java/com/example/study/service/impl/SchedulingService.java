package com.example.study.service.impl;


import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Component
public class SchedulingService {


    @Scheduled(cron = "*/10 * * * * ? ")
    public void testScheduling() {

        System.out.println("LocalDateTime.now() = " + LocalDateTime.now());

    }




}
