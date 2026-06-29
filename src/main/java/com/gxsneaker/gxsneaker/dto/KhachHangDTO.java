package com.gxsneaker.gxsneaker.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.Date;

@Data
public class KhachHangDTO {
    private Integer id;
    private String maKhachHang;
    private String hoTen;
    private String email;
    private String matKhau;
    private String soDienThoai;
    private Boolean gioiTinh;
    private LocalDate ngaySinh;
    private String avatar;
    private Boolean trangThai;
    private Boolean daXacThuc;
    private Date ngayTao;
    private Date ngayCapNhat;
}
