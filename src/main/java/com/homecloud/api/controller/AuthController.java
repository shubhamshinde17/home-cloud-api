package com.homecloud.api.controller;

import java.util.HashMap;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
                return ResponseEntity.ok(new AuthResponseDTO(true, "User registered successfully", null));
            } else {
                return ResponseEntity.status(400).body(new AuthResponseDTO(false, "User already exists", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new AuthResponseDTO(false, "Internal server error", null));
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
            return ResponseEntity.status(500).body(new AuthResponseDTO(false, "Internal server error", null));
        }
    }

    @GetMapping("/me")
    public ResponseEntity<UserDataDTO> getCurrentUser(Authentication authentication) {
        try {
            UserDataDTO response = new UserDataDTO(true, "User fetched successfully", null, null,
                    authentication.getName());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new UserDataDTO(false, "Internal server error", null, null, null));
        }
    }

}
