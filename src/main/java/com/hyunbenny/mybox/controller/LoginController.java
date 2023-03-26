package com.hyunbenny.mybox.controller;

import com.hyunbenny.mybox.dto.common.TokenInfo;
import com.hyunbenny.mybox.dto.request.LoginRequest;
import com.hyunbenny.mybox.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
public class LoginController {

    private final UserService userService;

    @GetMapping("/login")
    public ResponseEntity login(@RequestBody @Valid LoginRequest loginRequest) throws Exception {
        log.info("request : {}", loginRequest.toString());
        TokenInfo tokenInfo = userService.login(loginRequest);
        log.info("tokenInfo : {}", tokenInfo);
        return ResponseEntity.status(HttpStatus.OK)
                .body(tokenInfo);
    }

}
