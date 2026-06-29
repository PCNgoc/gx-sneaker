package com.gxsneaker.gxsneaker.dto;

import lombok.Data;
import java.util.Date;
import java.util.List;

@Data
public class GioHangDTO {
    private Integer id;
    private Integer idKhachHang;
    private Integer tongSoLuong;
    private Date ngayTao;
    private Date ngayCapNhat;
    private List<GioHangChiTietDTO> items;
}
