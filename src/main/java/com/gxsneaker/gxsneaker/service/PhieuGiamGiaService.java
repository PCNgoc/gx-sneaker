package com.gxsneaker.gxsneaker.service;

import com.gxsneaker.gxsneaker.dto.PhieuGiamGiaDTO;
import java.util.List;
import java.util.Optional;

public interface PhieuGiamGiaService {
    List<PhieuGiamGiaDTO> getAllPhieuGiamGia();
    Optional<PhieuGiamGiaDTO> getPhieuGiamGiaById(Integer id);
    Optional<PhieuGiamGiaDTO> getPhieuGiamGiaByMa(String maPhieu);
    PhieuGiamGiaDTO createPhieuGiamGia(PhieuGiamGiaDTO phieuGiamGia);
    PhieuGiamGiaDTO updatePhieuGiamGia(Integer id, PhieuGiamGiaDTO phieuGiamGia);
    void deletePhieuGiamGia(Integer id);
}

