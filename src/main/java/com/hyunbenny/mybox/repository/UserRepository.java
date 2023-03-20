package com.hyunbenny.mybox.repository;

import com.hyunbenny.mybox.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserAccount, Long> {

    Optional<UserAccount> findByUserId(String userId);
    Optional<UserAccount> findByUsername(String username);
}
