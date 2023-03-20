package com.hyunbenny.mybox.controller;

import com.hyunbenny.mybox.dto.UserDto;
import com.hyunbenny.mybox.dto.common.TokenInfo;
import com.hyunbenny.mybox.dto.request.LoginRequest;
import com.hyunbenny.mybox.service.UserService;
import com.hyunbenny.mybox.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
public class LoginController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;


    @GetMapping("/login")
    public ResponseEntity login(@RequestBody @Valid LoginRequest loginRequest) throws Exception {
        log.info("request : {}", loginRequest.toString());
        UserDto user = userService.login(loginRequest);
        TokenInfo tokenInfo = jwtTokenProvider.createToken(user);
        return ResponseEntity.ok()
                .body(tokenInfo);
    }

    @GetMapping("/logout")
    public ResponseEntity logout(@RequestParam Long id, HttpServletRequest request) {
        return null;
    }

}
