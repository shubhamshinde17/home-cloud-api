package com.homecloud.api.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import org.springframework.security.core.Authentication;

import com.homecloud.api.enums.AUTH_MESSAGES;
import com.homecloud.api.enums.COMMON_MESSAGES;
import com.homecloud.api.model.User;
import com.homecloud.api.repository.UserRepository;
import com.homecloud.api.security.JwtUtil;
import com.homecloud.api.transferobject.AuthResponseDTO;
import com.homecloud.api.transferobject.ResponseDTO;

@Service
public class AuthService {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(JwtUtil jwtUtil, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String generateToken(User user) {
        return jwtUtil.generateToken(user);
    }

    public String generateRefreshToken(User user) {
        return jwtUtil.generateRefreshToken(user);
    }

    public ResponseDTO<AuthResponseDTO, Void> authenticate(String email, String password) {
        var user = userRepository.findByEmail(email);
        if (user == null) {
            return new ResponseDTO<AuthResponseDTO, Void>(false, AUTH_MESSAGES.USER_NOT_FOUND.toString(),
                    Optional.empty(), Optional.empty());
        }
        if (verifyPassword(password, user.getPassword())) {
            String token = generateToken(user);
            String refreshToken = generateRefreshToken(user);
            return new ResponseDTO<AuthResponseDTO, Void>(true, AUTH_MESSAGES.AUTHENTICATION_SUCCESSFUL.toString(),
                    Optional.of(new AuthResponseDTO(token, refreshToken, 9000L)), Optional.empty());
        } else {
            return new ResponseDTO<AuthResponseDTO, Void>(false, AUTH_MESSAGES.INVALID_PASSWORD.toString(),
                    Optional.of(new AuthResponseDTO(null, null, null)), Optional.empty());
        }
    }

    public ResponseDTO<AuthResponseDTO, Void> refreshToken(String refreshToken) {
        try {
            if (jwtUtil.isTokenValid(refreshToken)) {
                String email = jwtUtil.getUsernameFromToken(refreshToken);
                User user = getUserByEmail(email);
                if (user != null) {
                    String newToken = generateToken(user);
                    String newRefreshToken = generateRefreshToken(user);
                    return new ResponseDTO<AuthResponseDTO, Void>(true, AUTH_MESSAGES.TOKEN_REFRESHED.toString(),
                            Optional.of(new AuthResponseDTO(newToken, newRefreshToken, 9000L)), Optional.empty());
                }
            }
            return new ResponseDTO<AuthResponseDTO, Void>(false, AUTH_MESSAGES.INVALID_REFRESH_TOKEN.toString(),
                    Optional.of(new AuthResponseDTO(null, null, null)), Optional.empty());
        } catch (Exception e) {
            return new ResponseDTO<AuthResponseDTO, Void>(false, COMMON_MESSAGES.INTERNAL_SERVER_ERROR.toString(),
                    Optional.of(new AuthResponseDTO(null, null, null)), Optional.empty());
        }
    }

    public User getCurrentUser(Authentication authentication) {
        String email = authentication.getName();
        return userRepository.findByEmail(email);
    }

    public boolean signupUser(String firstName, String lastName, String email, String password) {
        var existingUser = userRepository.findByEmail(email);
        if (existingUser != null) {
            return false;
        }
        String hashedPassword = passwordEncoder.encode(password);
        var newUser = new User();
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setEmail(email);
        newUser.setPassword(hashedPassword);
        userRepository.save(newUser);
        return true;
    }

    public boolean verifyPassword(String rawPassword, String hashedPassword) {
        return passwordEncoder.matches(rawPassword, hashedPassword);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void logout(Authentication authentication) {
        jwtUtil.invalidateToken(authentication);
    }
}
