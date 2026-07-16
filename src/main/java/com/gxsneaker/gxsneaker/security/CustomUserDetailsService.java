package com.gxsneaker.gxsneaker.security;

import com.gxsneaker.gxsneaker.entity.NhanVien;
import com.gxsneaker.gxsneaker.repository.NhanVienRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final NhanVienRepository nhanVienRepository;

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        NhanVien nhanVien = nhanVienRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Không tìm thấy tài khoản"));

        return new CustomUserDetails(nhanVien);
    }

}