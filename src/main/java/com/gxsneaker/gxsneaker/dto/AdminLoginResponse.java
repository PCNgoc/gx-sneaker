package com.gxsneaker.gxsneaker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdminLoginResponse {

    private Integer id;
    private String maNhanVien;
    private String hoTen;
    private String email;
    private String role;
    private String token;
    private String message;

}