package com.hyunbenny.mybox.service;

import com.hyunbenny.mybox.dto.UserDto;
import com.hyunbenny.mybox.dto.common.TokenInfo;
import com.hyunbenny.mybox.dto.request.JoinRequest;
import com.hyunbenny.mybox.dto.request.LoginRequest;
import com.hyunbenny.mybox.dto.request.PasswordModifyRequest;
import com.hyunbenny.mybox.entity.UserAccount;
import com.hyunbenny.mybox.exception.InvalidRequestException;
import com.hyunbenny.mybox.repository.UserRepository;
import com.hyunbenny.mybox.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;

    public UserDto join(JoinRequest joinRequest) {
        String encodedPassword = passwordEncoder.encode(joinRequest.getPassword());
        UserAccount userAccount = joinRequest.toEntity(encodedPassword);
        UserAccount savedUser = userRepository.save(userAccount);

        return new UserDto().fromEntity(savedUser);
    }

    public Optional<UserAccount> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public TokenInfo login(LoginRequest request) throws Exception {
        log.debug("loginParameter : {}, {}", request.toString());

        // Authentication 객체 생성
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(request.getUserId(), request.getPassword());

//        2. 실제 검증 (사용자 비밀번호 체크)이 이루어지는 부분
        // authenticate 매서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드가 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 검증된 인증정보를 통해 토큰을 생성해서 반환해준다.
        return jwtTokenProvider.generateToken(authentication);
    }

    public void modifyPassword(PasswordModifyRequest request) {
        UserAccount findUser = userRepository.findById(request.getId()).orElseThrow(() -> new InvalidRequestException("id", "해당 회원이 존재하지 않습니다."));

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
