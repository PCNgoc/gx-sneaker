package com.gxsneaker.gxsneaker.service;

import com.gxsneaker.gxsneaker.dto.KhachHangDTO;
import java.util.List;
import java.util.Optional;

public interface KhachHangService {
    List<KhachHangDTO> getAllKhachHang();
    Optional<KhachHangDTO> getKhachHangById(Integer id);
    KhachHangDTO createKhachHang(KhachHangDTO khachHang);
    KhachHangDTO updateKhachHang(Integer id, KhachHangDTO khachHang);
    void deleteKhachHang(Integer id);
}

