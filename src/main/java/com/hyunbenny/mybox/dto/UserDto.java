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

    private Long userNo;
    private String userId;
    private String username;
    private List<String> roles = new ArrayList<>();

    @Builder
    public UserDto(Long userNo, String userId, String username, List<String> roles) {
        this.userNo = userNo;
        this.userId = userId;
        this.username = username;
        this.roles = roles;
    }

    public UserDto fromEntity(UserAccount userAccount) {
        return UserDto.builder()
                .userNo(userAccount.getUserNo())
                .userId(userAccount.getUserId())
                .username(userAccount.getUsername())
                .roles(userAccount.getRoles())
                .build();
    }


}
