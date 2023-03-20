package com.hyunbenny.mybox.dto;

import com.hyunbenny.mybox.entity.UserAccount;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class UserDto {

    private Long id;
    private String userId;
    private String username;
    private List<String> roles = new ArrayList<>();

    @Builder
    public UserDto(Long id, String userId, String username, List<String> roles) {
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.roles = roles;
    }

    public UserDto fromEntity(UserAccount userAccount) {
        return UserDto.builder()
                .id(userAccount.getId())
                .userId(userAccount.getUserId())
                .username(userAccount.getUsername())
                .roles(userAccount.getRoles())
                .build();
    }


}
