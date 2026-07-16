package com.gxsneaker.gxsneaker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoanhThuTheoThangDTO {

    private Integer thang;
    private BigDecimal doanhThu;
}
