package com.homecloud.api.controller;

import java.util.HashMap;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.homecloud.api.enums.AUTH_MESSAGES;
import com.homecloud.api.enums.COMMON_MESSAGES;
import com.homecloud.api.model.User;
import com.homecloud.api.service.AuthService;
import com.homecloud.api.transferobject.AuthResponseDTO;
import com.homecloud.api.transferobject.ResponseDTO;
import com.homecloud.api.transferobject.UserDataDTO;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<ResponseDTO<AuthResponseDTO, Void>> signup(@RequestBody HashMap<String, String> user) {
        try {
            String firstName = user.get("firstName");
            String lastName = user.get("lastName");
            String email = user.get("email");
            String password = user.get("password");

            if (authService.signupUser(firstName, lastName, email, password)) {
                return ResponseEntity
                        .ok(new ResponseDTO<AuthResponseDTO, Void>(true, AUTH_MESSAGES.USER_REGISTERED.toString(),
                                new AuthResponseDTO(null, null, null), null));
            } else {
                return ResponseEntity.status(400)
                        .body(new ResponseDTO<AuthResponseDTO, Void>(false,
                                AUTH_MESSAGES.USER_ALREADY_EXISTS.toString(), new AuthResponseDTO(null, null, null),
                                null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(new ResponseDTO<AuthResponseDTO, Void>(false,
                            COMMON_MESSAGES.INTERNAL_SERVER_ERROR.toString(), new AuthResponseDTO(null, null, null),
                            null));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDTO<AuthResponseDTO, Void>> login(@RequestBody HashMap<String, String> user) {
        try {
            String email = user.get("email");
            String password = user.get("password");
            ResponseDTO<AuthResponseDTO, Void> response = authService.authenticate(email, password);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(new ResponseDTO<AuthResponseDTO, Void>(false,
                            COMMON_MESSAGES.INTERNAL_SERVER_ERROR.toString(), new AuthResponseDTO(null, null, null),
                            null));
        }
    }

    @GetMapping("/me")
    public ResponseEntity<ResponseDTO<UserDataDTO, Void>> getCurrentUser(Authentication authentication) {
        try {
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(401)
                        .body(new ResponseDTO<UserDataDTO, Void>(false, COMMON_MESSAGES.UNAUTHORIZED.toString(), null,
                                null));
            }
            User currentUser = authService.getCurrentUser(authentication);
            if (currentUser != null) {
                UserDataDTO response = new UserDataDTO(true, AUTH_MESSAGES.USER_FETCHED_SUCCESSFULLY.toString(),
                        currentUser.getFirstName(),
                        currentUser.getLastName(), currentUser.getEmail());
                return ResponseEntity.ok(new ResponseDTO<UserDataDTO, Void>(true,
                        AUTH_MESSAGES.USER_FETCHED_SUCCESSFULLY.toString(), response, null));
            } else {
                return ResponseEntity.status(404)
                        .body(new ResponseDTO<UserDataDTO, Void>(false, AUTH_MESSAGES.USER_NOT_FOUND.toString(), null,
                                null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(new ResponseDTO<UserDataDTO, Void>(false, COMMON_MESSAGES.INTERNAL_SERVER_ERROR.toString(),
                            null,
                            null));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<ResponseDTO<AuthResponseDTO, Void>> logout(Authentication authentication) {
        try {
            authService.logout(authentication);
            return ResponseEntity
                    .ok(new ResponseDTO<AuthResponseDTO, Void>(true, AUTH_MESSAGES.LOGOUT_SUCCESSFUL.toString(),
                            new AuthResponseDTO(null, null, null), null));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(new ResponseDTO<AuthResponseDTO, Void>(false,
                            COMMON_MESSAGES.INTERNAL_SERVER_ERROR.toString(), null, null));
        }
    }

}
