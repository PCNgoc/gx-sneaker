package com.gxsneaker.gxsneaker.dto;

import lombok.Data;
import java.util.Date;

@Data
public class GioHangChiTietDTO {
    private Integer id;
    private Integer idGioHang;
    private Long idChiTietSanPham;
    private Integer soLuong;
    private Date ngayThem;
    private ChiTietSanPhamDTO chiTietSanPham;
}
