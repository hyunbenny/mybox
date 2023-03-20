package com.hyunbenny.mybox.repository;

import com.hyunbenny.mybox.config.JpaConfig;
import com.hyunbenny.mybox.entity.UserAccount;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Import(JpaConfig.class)
@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("Database 연결 테스트")
public class UserRepositoryTest {

    private final UserRepository userRepository;

    public UserRepositoryTest(@Autowired UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @BeforeEach
    void setUserData() {
        for (int i = 1; i <= 10; i++) {
            userRepository.save(UserAccount.builder()
                            .userId("hyunbenny" + i)
                            .password("1234")
                            .username("hyunbin" + i)
                    .build());
        }
    }

    @AfterEach
    void clear() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("insert 테스트")
    void insertTest() {
        // given
        UserAccount userAccount = UserAccount.builder()
                .userId("hyunbenny")
                .password("1234")
                .username("hyunbin")
                .build();
        // when
        UserAccount savedUser = userRepository.save(userAccount);

        // then
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getUserId()).isEqualTo(userAccount.getUserId());
        assertThat(savedUser.getPassword()).isEqualTo(userAccount.getPassword());
        assertThat(savedUser.getUsername()).isEqualTo(userAccount.getUsername());
    }

    @Test
    @DisplayName("select 테스트")
    void selectTest() {
        // Given

        // When
        List<UserAccount> users = userRepository.findAll();

        // Then
        assertThat(users)
                .isNotNull()
                .hasSize(10);
    }

    @Test
    @DisplayName("update 테스트")
    void updateTest() {
        // given
        UserAccount userAccount = UserAccount.builder()
                .userId("hyunbenny")
                .password("1234")
                .username("hyunbin")
                .build();
        userRepository.save(userAccount);

        UserAccount findUser = userRepository.findByUserId("hyunbenny").get();
        System.out.println("userAccount : " + findUser.toString());

        // when
        findUser.changePassword("6789");
        UserAccount changedUser = userRepository.findByUserId("hyunbenny").get();

        // then
        System.out.println("changedUser : " + changedUser.toString());
        assertThat(changedUser.getPassword()).isEqualTo("6789");


    }

    @Test
    @DisplayName("delete 테스트")
    void deleteTest() {
        // given
        List<UserAccount> users = userRepository.findAll();

        // when
        userRepository.deleteById(users.get(0).getId());
        List<UserAccount> userListAfterDeleteUser = userRepository.findAll();

        // then
        assertThat(userListAfterDeleteUser.size()).isEqualTo(users.size() - 1);
    }
}
