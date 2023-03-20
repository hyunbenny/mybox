package com.hyunbenny.mybox.dto.common;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class TokenInfo {

    private String tokenType;
    private String accessToken;
    private String refreshToken;

    @Builder
    public TokenInfo(String tokenType, String accessToken, String refreshToken) {
        this.tokenType = tokenType;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
