package com.example.demo_1.Security.services;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.example.demo_1.Entity.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.fasterxml.jackson.annotation.JsonIgnore;
@Getter
@Setter
@ToString
public class UserDetailsImpl implements UserDetails{
    private static final long serialVersionUID = 1L;

    private Long uuid;

    private String username;

    private String email;

    @JsonIgnore
    private String password;

    private Collection<? extends GrantedAuthority> authorities;
    private String emergencyContactNo;

    public UserDetailsImpl(Long uuid, String username, String email, String password,
                           Collection<? extends GrantedAuthority> authorities,String emergencyContactNo) {
        this.uuid = uuid;
        this.username = username;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
        this.emergencyContactNo = emergencyContactNo;
    }

    public static UserDetailsImpl build(User user) {
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());

        return new UserDetailsImpl(
                user.getUuid(),
                user.getFirstname() +" "+user.getLastname(),
                user.getEmail(),
                user.getPassword(),
                authorities, user.getEmergencyContactNo());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
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

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(uuid, user.uuid);
    }
}
