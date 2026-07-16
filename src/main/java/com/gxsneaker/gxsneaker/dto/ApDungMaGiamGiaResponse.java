package com.gxsneaker.gxsneaker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ApDungMaGiamGiaResponse {

    private Long idPhieuGiamGia;

    private String maPhieuGiamGia;

    private String tenPhieu;

    private BigDecimal soTienGiam;

    private BigDecimal tongTienSauGiam;

    private String message;
}