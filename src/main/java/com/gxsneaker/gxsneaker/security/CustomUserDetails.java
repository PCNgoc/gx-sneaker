package com.gxsneaker.gxsneaker.security;

import com.gxsneaker.gxsneaker.entity.NhanVien;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@AllArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final NhanVien nhanVien;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return List.of(
                new SimpleGrantedAuthority(
                        "ROLE_" +
                                nhanVien.getPhanQuyen().getTenQuyen().toUpperCase()
                )
        );

    }

    @Override
    public String getPassword() {
        return nhanVien.getMatKhau();
    }

    @Override
    public String getUsername() {
        return nhanVien.getEmail();
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

        return nhanVien.getTrangThai();

    }

    public NhanVien getNhanVien() {

        return nhanVien;

    }

}