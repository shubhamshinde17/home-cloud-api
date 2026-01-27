package com.homecloud.api.transferobject;

public class AuthResponseDTO extends ResponseDTO {
    private String token;

    public AuthResponseDTO(boolean success, String message, String token) {
        super(success, message);
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
