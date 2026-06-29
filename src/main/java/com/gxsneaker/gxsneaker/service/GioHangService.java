package com.gxsneaker.gxsneaker.service;

import com.gxsneaker.gxsneaker.dto.GioHangDTO;
import com.gxsneaker.gxsneaker.dto.GioHangChiTietDTO;

public interface GioHangService {
    GioHangDTO getGioHangByKhachHangId(Integer khachHangId);
    GioHangChiTietDTO addItemToCart(Integer khachHangId, Long chiTietSanPhamId, Integer soLuong);
    GioHangChiTietDTO updateItemQuantity(Integer cartItemId, Integer soLuong);
    void removeItemFromCart(Integer cartItemId);
    void clearCart(Integer khachHangId);
}

