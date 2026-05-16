package com.homecloud.api.graphql;

import org.springframework.stereotype.Controller;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;

import com.homecloud.api.service.AuthService;
import com.homecloud.api.transferobject.AuthResponseDTO;
import com.homecloud.api.transferobject.ResponseDTO;
import com.homecloud.api.transferobject.input.LoginInput;

@Controller
public class AuthGraphQLController {

    private final AuthService authService;

    public AuthGraphQLController(AuthService authService) {
        this.authService = authService;
    }

    @QueryMapping
    public ResponseDTO<AuthResponseDTO, Void> authenticate(
            @Argument LoginInput input) {

        return authService.authenticate(input.getEmail(), input.getPassword());
    }

}
