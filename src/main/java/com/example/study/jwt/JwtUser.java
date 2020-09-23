package com.example.study.jwt;

import com.example.study.model.SysUser;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;


@Data
public class JwtUser implements UserDetails {


    private Integer id;
    private String username;

    private String password;

    private String phone;

    private String remake;

    private Collection<? extends GrantedAuthority> authorities; //封装认证


    public JwtUser(SysUser user, Collection<? extends GrantedAuthority> authorities) {
        this.id = user.getId();
        this.username = user.getUserName();
        this.password = user.getPassword();
        this.phone = user.getPhone();
        this.remake = user.getRemake();
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
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
