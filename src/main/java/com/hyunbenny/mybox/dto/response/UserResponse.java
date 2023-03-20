package com.hyunbenny.mybox.dto.response;

import com.hyunbenny.mybox.dto.UserDto;

public class UserResponse {
    private UserDto user;

    private String code;
    private String message;

    public UserResponse(UserDto user, String code, String message) {
        this.user = user;
        this.code = code;
        this.message = message;
    }

    public UserDto getUser() {
        return user;
    }
    public String getCode() {return code;}
    public String getMessage() {
        return message;
    }
}
