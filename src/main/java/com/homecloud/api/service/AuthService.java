package com.homecloud.api.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;

import com.homecloud.api.model.User;
import com.homecloud.api.repository.UserRepository;
import com.homecloud.api.security.JwtUtil;
import com.homecloud.api.transferobject.AuthResponseDTO;

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

    public AuthResponseDTO authenticate(String email, String password) {
        var user = userRepository.findByEmail(email);
        if (user == null) {
            return new AuthResponseDTO(false, "User not found", null, password, null);
        }
        if (verifyPassword(password, user.getPassword())) {
            String token = jwtUtil.generateToken(user);
            String refreshToken = jwtUtil.generateRefreshToken(user);
            return new AuthResponseDTO(true, "Authentication successful", token, refreshToken, 9000L);
        } else {
            return new AuthResponseDTO(false, "Invalid password", null, password, null);
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
