package com.homecloud.api.service;

import org.springframework.stereotype.Service;

import com.homecloud.api.model.TokenLog;
import com.homecloud.api.repository.TokenLogRepository;

@Service
public class TokenLogService {

    private final TokenLogRepository tokenLogRepository;

    public TokenLogService(TokenLogRepository tokenLogRepository) {
        this.tokenLogRepository = tokenLogRepository;
    }

    public TokenLog saveTokenLog(TokenLog tokenLog) {
        if (tokenLog.getTokenValue() != null && !tokenLog.getTokenValue().isEmpty()) {
            return tokenLogRepository.save(tokenLog);
        }
        return null;
    }

    public boolean deleteByUserId(Long userId) {
        try {
            tokenLogRepository.deleteByUserId(userId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
