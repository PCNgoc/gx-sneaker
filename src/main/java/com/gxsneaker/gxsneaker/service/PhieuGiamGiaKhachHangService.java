package com.gxsneaker.gxsneaker.service;

import com.gxsneaker.gxsneaker.dto.PhieuGiamGiaKhachHangDTO;
import java.util.List;
import java.util.Optional;

public interface PhieuGiamGiaKhachHangService {
    List<PhieuGiamGiaKhachHangDTO> getVouchersByKhachHangId(Integer khachHangId);
    PhieuGiamGiaKhachHangDTO assignVoucherToKhachHang(Integer khachHangId, Integer phieuGiamGiaId);
    PhieuGiamGiaKhachHangDTO markAsUsed(Integer id);
    Optional<PhieuGiamGiaKhachHangDTO> getById(Integer id);
}

