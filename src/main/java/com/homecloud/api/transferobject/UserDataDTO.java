package com.homecloud.api.transferobject;

public class UserDataDTO extends ResponseDTO {
    private String firstName;
    private String lastName;
    private String email;

    public UserDataDTO(boolean success, String message, String firstName, String lastName, String email) {
        super(success, message);
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
