package com.gxsneaker.gxsneaker.dto;

import lombok.Data;
import java.util.Date;

@Data
public class DiaChiDTO {
    private Integer id;
    private Integer idKhachHang;
    private String hoTenNguoiNhan;
    private String soDienThoai;
    private String tinhThanh;
    private String quanHuyen;
    private String phuongXa;
    private String diaChiChiTiet;
    private Date ngayTao;
    private Date ngayCapNhat;
}
