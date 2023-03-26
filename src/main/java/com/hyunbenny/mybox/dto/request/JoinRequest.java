package com.hyunbenny.mybox.dto.request;

import com.hyunbenny.mybox.entity.UserAccount;
import lombok.*;
import org.hibernate.validator.constraints.NotBlank;

import java.util.Collections;

@Getter
@NoArgsConstructor
@ToString
public class JoinRequest {

    @NotBlank(message = "{userId.notNull}")
    private String userId;

    @NotBlank(message = "{password.notNull}")
    private String password;

    @NotBlank(message = "{username.notNull}")
    private String username;

    @Builder
    public JoinRequest(String userId, String password, String username) {
        this.userId = userId;
        this.password = password;
        this.username = username;
    }

    public UserAccount toEntity(String encodedPassword) {
        return UserAccount.builder()
                .userId(userId)
                .password(encodedPassword)
                .username(username)
                .roles(Collections.singletonList("ROLE_USER"))
                .build();

    }

}
