package com.gxsneaker.gxsneaker.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponseDTO {

    private Long id;

    private String maHoaDon;

    private String trangThai;

    private Date ngayDatHang;

    private String tenNguoiNhan;

    private String soDienThoai;

    private String diaChi;

    private BigDecimal tongTien;

    private List<OrderItemResponseDTO> items;
}