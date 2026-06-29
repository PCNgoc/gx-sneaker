package com.gxsneaker.gxsneaker.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class PhieuGiamGiaDTO {
    private Integer id;
    private String maPhieu;
    private String tenPhieu;
    private Boolean loaiGiamGia;
    private BigDecimal giaTriGiam;
    private BigDecimal giaTriDonHangToiThieu;
    private BigDecimal giaTriGiamToiDa;
    private Integer soLuong;
    private Date ngayBatDau;
    private Date ngayKetThuc;
    private Boolean trangThai;
    private Date ngayTao;
    private Date ngayCapNhat;
}
