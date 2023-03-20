package com.hyunbenny.mybox.service;

import com.hyunbenny.mybox.dto.UserDto;
import com.hyunbenny.mybox.dto.request.JoinRequest;
import com.hyunbenny.mybox.dto.request.LoginRequest;
import com.hyunbenny.mybox.dto.request.PasswordModifyRequest;
import com.hyunbenny.mybox.entity.UserAccount;
import com.hyunbenny.mybox.exception.InvalidRequestException;
import com.hyunbenny.mybox.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public UserDto join(JoinRequest joinRequest) {
        String encodedPassword = passwordEncoder.encode(joinRequest.getPassword());

        UserAccount userAccount = joinRequest.toEntity(encodedPassword);

        UserAccount savedUser = userRepository.save(userAccount);
        return new UserDto().fromEntity(savedUser);
    }

    public Optional<UserAccount> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public UserDto login(LoginRequest request) throws Exception {
        log.debug("loginParameter : {}, {}", request.toString());
        UserAccount findUser = userRepository.findByUserId(request.getUserId()).orElseThrow(() -> new InvalidRequestException(request.getUserId(), "해당 회원이 존재하지 않습니다."));

        if (findUser != null) {
            if (passwordEncoder.matches(request.getPassword(), findUser.getPassword())) {
                return new UserDto().fromEntity(findUser);
            }else{
                log.info("올바르지 않은 비밀번호");
                throw new InvalidRequestException("password", "올바르지 않은 비밀번호입니다.");
            }
        }
        log.info("올바르지 않은 아이디");
        throw new InvalidRequestException("userId", "올바르지 않은 아이디입니다..");
    }

    public void modifyPassword(PasswordModifyRequest request) {
        UserAccount findUser = userRepository.findById(request.getId()).orElseThrow(() -> new InvalidRequestException("id", "해당 회원이 존재하지 않습니다."));
        log.info("findUser : {}", findUser.toString());
        log.info("request : {}", request);

        if (isSamePassword(request.getCurrPassword(), findUser.getPassword())) {
            log.info("same password");
            findUser.changePassword(passwordEncoder.encode(request.getNewPassword()));
        }else{
            log.info("wrong password");
            throw new InvalidRequestException("password", "현재 비밀번호가 틀렸습니다.");
        }
    }
    public boolean isSamePassword(String currPassword, String newPassword) {
        log.info("currPassword : {}, newPassword : {}", currPassword, newPassword);
        return passwordEncoder.matches(currPassword, newPassword);
    }
}
