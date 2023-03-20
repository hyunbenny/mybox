package com.hyunbenny.mybox.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class PasswordModifyRequest {

    private Long id;

    private String currPassword;

    private String newPassword;

    @Builder
    public PasswordModifyRequest(Long id, String currPassword, String newPassword) {
        this.id = id;
        this.currPassword = currPassword;
        this.newPassword = newPassword;
    }




}
