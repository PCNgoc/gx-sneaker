package com.gxsneaker.gxsneaker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NhanVienDTO {

    private Integer id;

    private Integer idPhanQuyen;

    private String tenQuyen;

    private String maNhanVien;

    private String hoTen;

    private String email;

    private String matKhau;

    private String soDienThoai;

    private Boolean gioiTinh;

    private String diaChi;

    private String anhDaiDien;

    private Boolean trangThai;

}