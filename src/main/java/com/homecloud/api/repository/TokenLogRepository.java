package com.homecloud.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.homecloud.api.model.TokenLog;

public interface TokenLogRepository extends JpaRepository<TokenLog, Long> {

    public void deleteByUserId(Long userId);

}
