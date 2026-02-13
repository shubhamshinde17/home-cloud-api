package com.homecloud.api.transferobject;

public class AuthResponseDTO extends ResponseDTO {
    private String accessToken;
    private String refreshToken;
    private Long expiresIn;

    public AuthResponseDTO(boolean success, String message, String accessToken, String refreshToken, Long expiresIn) {
        super(success, message);
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiresIn = expiresIn;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }

}
