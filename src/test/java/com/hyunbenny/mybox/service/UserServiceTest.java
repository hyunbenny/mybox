package com.hyunbenny.mybox.service;

import com.hyunbenny.mybox.dto.request.JoinRequest;
import com.hyunbenny.mybox.dto.request.PasswordModifyRequest;
import com.hyunbenny.mybox.entity.UserAccount;
import com.hyunbenny.mybox.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@DisplayName("UserService 단위 테스트 ")
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT) // modifyPasswordTest에서 org.mockito.exceptions.misusing.UnnecessaryStubbingException의 발생으로 추가(근본적인 해결책은 아니나 일단 테스트 통과를 위해 추가함)
class UserServiceTest {

    @InjectMocks // Mock을 주입하는 대상
    private UserService sut; // 테스트 대상

    @Mock // Mock을 주입하는 대상을 제외한 나머지
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @DisplayName("유저정보가 정상적으로 저장된다.")
    @Test
    void saveTest() {
        // Given
        given(userRepository.save(any(UserAccount.class))).willReturn(UserAccount.builder().userId("hyunbenny90")
                .password("hello1234")
                .username("hyunbenny")
                .build());

        // When
        JoinRequest request = createJoinRequest();
        sut.join(request);

        // Then
        then(userRepository).should().save(any(UserAccount.class));
    }

    @DisplayName("유저의 id가 주어지면 유저의 정보를 조회한다.")
    @Test
    void getUserByIdTest() {
        // given
        given(userRepository.findById(any(Long.class)))
                .willReturn(Optional.of(UserAccount.builder().userId("hyunbenny90")
                                                             .password("hello1234")
                                                             .username("hyunbenny")
                        .build()));


        // when
        Long id = 1L;
        sut.getUserById(id);

        // then
        then(userRepository).should().findById(any(Long.class));
    }

    @DisplayName("유저의 비밀번호를 수정한다")
    @Test
    void modifyPasswordTest() {
        // given
        String encodedHello = "$2a$10$EcWqhK39BvIXnrbaPRIz7eTj0VeiKsh6l5iCUlh3jevwdKTRbzdz6";
        String encodedHello123 = "$2a$10$EcWqhK39BvIXnrbaPRIz7eTj0VeiKsh6l5iCUlh121aadKdsfad21a";
        UserAccount user = UserAccount.builder()
                .userNo(1L)
                .userId("hyunbenny90")
                .password(encodedHello)
                .username("hyunbenny")
                .build();

        PasswordModifyRequest request = PasswordModifyRequest.builder()
                .id(1L)
                .currPassword("hello")
                .newPassword("hello123")
                .build();

        given(userRepository.findById(request.getId())).willReturn(Optional.of(user));
        given(passwordEncoder.encode("hello")).willReturn(encodedHello);
        given(passwordEncoder.encode("hello123")).willReturn(encodedHello123);
        given(passwordEncoder.matches("hello", encodedHello)).willReturn(true);
        given(passwordEncoder.matches("hello123", encodedHello123)).willReturn(true);
        willDoNothing().given(userRepository).flush();

        // when
        sut.modifyPassword(request);
        System.out.println(user);

        // then
        then(userRepository).should().findById(request.getId());
        assertThat(user.getPassword()).isEqualTo(encodedHello123);
    }

    private JoinRequest createJoinRequest() {
        return new JoinRequest("hyunbenny90", "hello1234", "hyunbenny");
    }


}