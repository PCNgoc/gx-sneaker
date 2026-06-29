package com.gxsneaker.gxsneaker.service.impl;

import com.gxsneaker.gxsneaker.dto.GioHangDTO;
import com.gxsneaker.gxsneaker.dto.GioHangChiTietDTO;
import com.gxsneaker.gxsneaker.entity.ChiTietSanPham;
import com.gxsneaker.gxsneaker.entity.GioHang;
import com.gxsneaker.gxsneaker.entity.GioHangChiTiet;
import com.gxsneaker.gxsneaker.entity.KhachHang;
import com.gxsneaker.gxsneaker.mapper.GioHangMapper;
import com.gxsneaker.gxsneaker.mapper.GioHangChiTietMapper;
import com.gxsneaker.gxsneaker.repository.ChiTietSanPhamRepository;
import com.gxsneaker.gxsneaker.repository.GioHangChiTietRepository;
import com.gxsneaker.gxsneaker.repository.GioHangRepository;
import com.gxsneaker.gxsneaker.repository.KhachHangRepository;
import com.gxsneaker.gxsneaker.service.GioHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;

@Service
@Transactional
public class GioHangServiceImpl implements GioHangService {

    @Autowired
    private GioHangRepository gioHangRepository;

    @Autowired
    private GioHangChiTietRepository gioHangChiTietRepository;

    @Autowired
    private KhachHangRepository khachHangRepository;

    @Autowired
    private ChiTietSanPhamRepository chiTietSanPhamRepository;

    private GioHang getGioHangEntityByKhachHangId(Integer khachHangId) {
        Optional<GioHang> existingCart = gioHangRepository.findAll().stream()
                .filter(gh -> gh.getKhachHang() != null && gh.getKhachHang().getId().equals(khachHangId))
                .findFirst();

        if (existingCart.isPresent()) {
            return existingCart.get();
        }

        KhachHang khachHang = khachHangRepository.findById(khachHangId)
                .orElseThrow(() -> new RuntimeException("KhachHang not found with id " + khachHangId));

        GioHang newCart = GioHang.builder()
                .khachHang(khachHang)
                .tongSoLuong(0)
                .gioHangChiTiets(new ArrayList<>())
                .build();

        return gioHangRepository.save(newCart);
    }

    @Override
    public GioHangDTO getGioHangByKhachHangId(Integer khachHangId) {
        GioHang cart = getGioHangEntityByKhachHangId(khachHangId);
        return GioHangMapper.toDTO(cart);
    }

    @Override
    public GioHangChiTietDTO addItemToCart(Integer khachHangId, Long chiTietSanPhamId, Integer soLuong) {
        GioHang cart = getGioHangEntityByKhachHangId(khachHangId);
        ChiTietSanPham productDetail = chiTietSanPhamRepository.findById(chiTietSanPhamId)
                .orElseThrow(() -> new RuntimeException("ChiTietSanPham not found with id " + chiTietSanPhamId));

        Optional<GioHangChiTiet> existingItem = cart.getGioHangChiTiets().stream()
                .filter(item -> item.getChiTietSanPham().getId().equals(chiTietSanPhamId))
                .findFirst();

        GioHangChiTiet cartItem;
        if (existingItem.isPresent()) {
            cartItem = existingItem.get();
            cartItem.setSoLuong(cartItem.getSoLuong() + soLuong);
        } else {
            cartItem = GioHangChiTiet.builder()
                    .gioHang(cart)
                    .chiTietSanPham(productDetail)
                    .soLuong(soLuong)
                    .build();
            cart.getGioHangChiTiets().add(cartItem);
        }

        GioHangChiTiet savedItem = gioHangChiTietRepository.save(cartItem);
        updateCartTotalQuantity(cart);
        
        return GioHangChiTietMapper.toDTO(savedItem);
    }

    @Override
    public GioHangChiTietDTO updateItemQuantity(Integer cartItemId, Integer soLuong) {
        GioHangChiTiet cartItem = gioHangChiTietRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found with id " + cartItemId));

        cartItem.setSoLuong(soLuong);
        GioHangChiTiet saved = gioHangChiTietRepository.save(cartItem);

        updateCartTotalQuantity(cartItem.getGioHang());
        return GioHangChiTietMapper.toDTO(saved);
    }

    @Override
    public void removeItemFromCart(Integer cartItemId) {
        GioHangChiTiet cartItem = gioHangChiTietRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found with id " + cartItemId));

        GioHang cart = cartItem.getGioHang();
        cart.getGioHangChiTiets().remove(cartItem);
        gioHangChiTietRepository.delete(cartItem);

        updateCartTotalQuantity(cart);
    }

    @Override
    public void clearCart(Integer khachHangId) {
        GioHang cart = getGioHangEntityByKhachHangId(khachHangId);
        gioHangChiTietRepository.deleteAll(cart.getGioHangChiTiets());
        cart.getGioHangChiTiets().clear();
        cart.setTongSoLuong(0);
        gioHangRepository.save(cart);
    }

    private void updateCartTotalQuantity(GioHang cart) {
        int total = cart.getGioHangChiTiets().stream()
                .mapToInt(GioHangChiTiet::getSoLuong)
                .sum();
        cart.setTongSoLuong(total);
        gioHangRepository.save(cart);
    }
}
