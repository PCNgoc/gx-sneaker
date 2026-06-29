package com.gxsneaker.gxsneaker.mapper;

import com.gxsneaker.gxsneaker.dto.PhieuGiamGiaKhachHangDTO;
import com.gxsneaker.gxsneaker.entity.KhachHang;
import com.gxsneaker.gxsneaker.entity.PhieuGiamGia;
import com.gxsneaker.gxsneaker.entity.PhieuGiamGiaKhachHang;

public class PhieuGiamGiaKhachHangMapper {

    public static PhieuGiamGiaKhachHangDTO toDTO(PhieuGiamGiaKhachHang pggh) {
        if (pggh == null) return null;
        PhieuGiamGiaKhachHangDTO dto = new PhieuGiamGiaKhachHangDTO();
        dto.setId(pggh.getId());
        if (pggh.getKhachHang() != null) {
            dto.setIdKhachHang(pggh.getKhachHang().getId());
        }
        if (pggh.getPhieuGiamGia() != null) {
            dto.setIdPhieuGiamGia(pggh.getPhieuGiamGia().getId());
            dto.setMaPhieu(pggh.getPhieuGiamGia().getMaPhieu());
            dto.setTenPhieu(pggh.getPhieuGiamGia().getTenPhieu());
        }
        dto.setNgayNhan(pggh.getNgayNhan());
        dto.setDaSuDung(pggh.getDaSuDung());
        return dto;
    }

    public static PhieuGiamGiaKhachHang toEntity(PhieuGiamGiaKhachHangDTO dto) {
        if (dto == null) return null;
        PhieuGiamGiaKhachHang pggh = new PhieuGiamGiaKhachHang();
        pggh.setId(dto.getId());
        if (dto.getIdKhachHang() != null) {
            KhachHang kh = new KhachHang();
            kh.setId(dto.getIdKhachHang());
            pggh.setKhachHang(kh);
        }
        if (dto.getIdPhieuGiamGia() != null) {
            PhieuGiamGia p = new PhieuGiamGia();
            p.setId(dto.getIdPhieuGiamGia());
            pggh.setPhieuGiamGia(p);
        }
        pggh.setNgayNhan(dto.getNgayNhan());
        pggh.setDaSuDung(dto.getDaSuDung());
        return pggh;
    }
}
