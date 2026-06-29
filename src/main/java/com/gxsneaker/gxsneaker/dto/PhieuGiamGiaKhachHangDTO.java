package com.gxsneaker.gxsneaker.dto;

import lombok.Data;
import java.util.Date;

@Data
public class PhieuGiamGiaKhachHangDTO {
    private Integer id;
    private Integer idKhachHang;
    private Integer idPhieuGiamGia;
    private Date ngayNhan;
    private Boolean daSuDung;
    private String maPhieu;
    private String tenPhieu;
}
