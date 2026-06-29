package com.gxsneaker.gxsneaker.service;

import com.gxsneaker.gxsneaker.dto.DiaChiDTO;
import java.util.List;
import java.util.Optional;

public interface DiaChiService {
    List<DiaChiDTO> getDiaChiByKhachHangId(Integer khachHangId);
    Optional<DiaChiDTO> getDiaChiById(Integer id);
    DiaChiDTO createDiaChi(DiaChiDTO diaChi);
    DiaChiDTO updateDiaChi(Integer id, DiaChiDTO diaChi);
    void deleteDiaChi(Integer id);
}

