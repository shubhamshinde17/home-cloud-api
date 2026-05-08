package com.homecloud.api.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class Config {

    @Value("${storage.path}")
    private String storagePath;

    @Value("${version:unknown}")
    private String version;

    @Value("${aes.key}")
    private String aesKey;

    public String getStoragePath() {
        return storagePath;
    }

    public String getVersion() {
        return version;
    }

    public String getAesKey() {
        return aesKey;
    }

}
