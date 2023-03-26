package com.hyunbenny.mybox.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;

@Getter
@ToString
@AllArgsConstructor
public class LoginRequest {

    @NotBlank(message = "{userId.notNull}")
    private String userId;

    @NotBlank(message = "{password.notNull}")
    private String password;

}
