package com.gxsneaker.gxsneaker.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemResponseDTO {

    private Long chiTietSanPhamId;

    private String productName;

    private String image;

    private String color;

    private Integer size;

    private Integer quantity;

    private BigDecimal price;

    private BigDecimal total;
}