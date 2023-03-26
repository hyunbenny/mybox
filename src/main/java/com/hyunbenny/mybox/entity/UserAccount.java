package com.hyunbenny.mybox.entity;

import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
@Getter
@Table(indexes = {
        @Index(columnList = "userId"),
        @Index(columnList = "username"),
        @Index(columnList = "createdAt")
})
public class UserAccount extends AuditingFields implements UserDetails {

    @Id
    @Column(name = "user_no")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userNo;

    @Column(unique = true)
    private String userId;

    private String password;

    private String username;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles = new ArrayList<>();

    public UserAccount() {
    }

    @Builder
    public UserAccount(Long userNo, String userId, String password, String username, List<String> roles) {
        this.userNo = userNo;
        this.userId = userId;
        this.password = password;
        this.username = username;
        this.roles = roles;
    }

    public void changePassword(String newPassword) {
        this.password = newPassword;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserAccount that)) return false;
        return Objects.equals(userNo, that.userNo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userNo);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
