package com.hyunbenny.mybox.controller;

import com.hyunbenny.mybox.dto.request.JoinRequest;
import com.hyunbenny.mybox.dto.UserDto;
import com.hyunbenny.mybox.dto.request.PasswordModifyRequest;
import com.hyunbenny.mybox.entity.UserAccount;
import com.hyunbenny.mybox.exception.InvalidRequestException;
import com.hyunbenny.mybox.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/sign-up")
    public ResponseEntity signUp(@RequestBody @Valid JoinRequest joinRequest) {
        log.info("request : {}", joinRequest.toString());
        UserDto savedUser = userService.join(joinRequest);
        return ResponseEntity.status(HttpStatus.OK)
                .body(savedUser);
    }

    @PostMapping("/modifyPassword")
    public ResponseEntity modifyPassword(@RequestBody @Valid PasswordModifyRequest request) {
        log.info("request : {}", request.toString());

        userService.modifyPassword(request);

        return ResponseEntity.status(HttpStatus.OK)
                .body("");
    }

    @GetMapping("/getUserInfo")
    public ResponseEntity getUserInfo(@RequestParam(value = "id") Long id) {
        UserAccount findUser = userService.getUserById(id).orElseThrow(() -> new InvalidRequestException(String.valueOf(id), "해당 회원이 존재하지 않습니다."));
        return ResponseEntity.status(HttpStatus.OK)
                .body(findUser);
    }

}
