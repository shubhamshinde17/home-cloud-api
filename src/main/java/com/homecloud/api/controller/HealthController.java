package com.homecloud.api.controller;

import org.springframework.web.bind.annotation.RestController;

import com.homecloud.api.service.Config;

import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class HealthController {

    private Config config;

    public HealthController(Config config) {
        this.config = config;
    }

    @GetMapping("/health")
    public String health() {
        return "Home Cloud Backend is running 🚀 (Version: " + config.getVersion() + ")";
    }

}
