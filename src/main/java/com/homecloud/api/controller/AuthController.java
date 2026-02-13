package com.homecloud.api.controller;

import java.util.HashMap;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.homecloud.api.model.User;
import com.homecloud.api.service.AuthService;
import com.homecloud.api.transferobject.AuthResponseDTO;
import com.homecloud.api.transferobject.UserDataDTO;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponseDTO> signup(@RequestBody HashMap<String, String> user) {
        try {
            String firstName = user.get("firstName");
            String lastName = user.get("lastName");
            String email = user.get("email");
            String password = user.get("password");

            if (authService.signupUser(firstName, lastName, email, password)) {
                return ResponseEntity.ok(new AuthResponseDTO(true, "User registered successfully", null, null, null));
            } else {
                return ResponseEntity.status(400)
                        .body(new AuthResponseDTO(false, "User already exists", null, null, null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(new AuthResponseDTO(false, "Internal server error", null, null, null));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody HashMap<String, String> user) {
        try {
            String email = user.get("email");
            String password = user.get("password");
            AuthResponseDTO response = authService.authenticate(email, password);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(new AuthResponseDTO(false, "Internal server error", null, null, null));
        }
    }

    @GetMapping("/me")
    public ResponseEntity<UserDataDTO> getCurrentUser(Authentication authentication) {
        try {
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(401)
                        .body(new UserDataDTO(false, "Unauthorized", null, null, null));
            }
            User currentUser = authService.getCurrentUser(authentication);
            if (currentUser != null) {
                UserDataDTO response = new UserDataDTO(true, "User fetched successfully", currentUser.getFirstName(),
                        currentUser.getLastName(), currentUser.getEmail());
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(404).body(new UserDataDTO(false, "User not found", null, null, null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(new UserDataDTO(false, "Internal server error: " + e.getMessage(), null, null, null));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<AuthResponseDTO> logout(Authentication authentication) {
        try {
            authService.logout(authentication);
            return ResponseEntity.ok(new AuthResponseDTO(true, "User logged out successfully", null, null, null));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(new AuthResponseDTO(false, "Internal server error", null, null, null));
        }
    }

}
