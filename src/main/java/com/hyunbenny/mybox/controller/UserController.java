package com.hyunbenny.mybox.controller;

import com.hyunbenny.mybox.dto.request.JoinRequest;
import com.hyunbenny.mybox.dto.UserDto;
import com.hyunbenny.mybox.dto.request.PasswordModifyRequest;
import com.hyunbenny.mybox.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/sign-up")
    public ResponseEntity signUp(@RequestBody @Valid JoinRequest joinRequest) {
        log.info("request : {}", joinRequest.toString());
        UserDto savedUser = userService.join(joinRequest);
        return ResponseEntity.ok()
                .header(HttpHeaders.ACCEPT)
                .body(savedUser);
    }

    @PostMapping("/modifyPassword")
    public ResponseEntity modifyPassword(@RequestBody @Valid PasswordModifyRequest request) {
        log.info("request : {}", request.toString());

        userService.modifyPassword(request);

        return ResponseEntity.ok()
                .header(HttpHeaders.ACCEPT)
                .body("");
    }


}
