package com.gxsneaker.gxsneaker.mapper;

import com.gxsneaker.gxsneaker.dto.PhieuGiamGiaDTO;
import com.gxsneaker.gxsneaker.entity.PhieuGiamGia;

public class PhieuGiamGiaMapper {

    public static PhieuGiamGiaDTO toDTO(PhieuGiamGia p) {
        if (p == null) return null;
        PhieuGiamGiaDTO dto = new PhieuGiamGiaDTO();
        dto.setId(p.getId());
        dto.setMaPhieu(p.getMaPhieu());
        dto.setTenPhieu(p.getTenPhieu());
        dto.setLoaiGiamGia(p.getLoaiGiamGia());
        dto.setGiaTriGiam(p.getGiaTriGiam());
        dto.setGiaTriDonHangToiThieu(p.getGiaTriDonHangToiThieu());
        dto.setGiaTriGiamToiDa(p.getGiaTriGiamToiDa());
        dto.setSoLuong(p.getSoLuong());
        dto.setNgayBatDau(p.getNgayBatDau());
        dto.setNgayKetThuc(p.getNgayKetThuc());
        dto.setTrangThai(p.getTrangThai());
        dto.setNgayTao(p.getNgayTao());
        dto.setNgayCapNhat(p.getNgayCapNhat());
        return dto;
    }

    public static PhieuGiamGia toEntity(PhieuGiamGiaDTO dto) {
        if (dto == null) return null;
        PhieuGiamGia p = new PhieuGiamGia();
        p.setId(dto.getId());
        p.setMaPhieu(dto.getMaPhieu());
        p.setTenPhieu(dto.getTenPhieu());
        p.setLoaiGiamGia(dto.getLoaiGiamGia());
        p.setGiaTriGiam(dto.getGiaTriGiam());
        p.setGiaTriDonHangToiThieu(dto.getGiaTriDonHangToiThieu());
        p.setGiaTriGiamToiDa(dto.getGiaTriGiamToiDa());
        p.setSoLuong(dto.getSoLuong());
        p.setNgayBatDau(dto.getNgayBatDau());
        p.setNgayKetThuc(dto.getNgayKetThuc());
        p.setTrangThai(dto.getTrangThai());
        p.setNgayTao(dto.getNgayTao());
        p.setNgayCapNhat(dto.getNgayCapNhat());
        return p;
    }
}
