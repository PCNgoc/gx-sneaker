package com.gxsneaker.gxsneaker.mapper;

import com.gxsneaker.gxsneaker.dto.KhachHangDTO;
import com.gxsneaker.gxsneaker.entity.KhachHang;

public class KhachHangMapper {

    public static KhachHangDTO toDTO(KhachHang kh) {
        if (kh == null) return null;
        KhachHangDTO dto = new KhachHangDTO();
        dto.setId(kh.getId());
        dto.setMaKhachHang(kh.getMaKhachHang());
        dto.setHoTen(kh.getHoTen());
        dto.setEmail(kh.getEmail());
        dto.setMatKhau(kh.getMatKhau());
        dto.setSoDienThoai(kh.getSoDienThoai());
        dto.setGioiTinh(kh.getGioiTinh());
        dto.setNgaySinh(kh.getNgaySinh());
        dto.setAvatar(kh.getAvatar());
        dto.setTrangThai(kh.getTrangThai());
        dto.setDaXacThuc(kh.getDaXacThuc());
        dto.setNgayTao(kh.getNgayTao());
        dto.setNgayCapNhat(kh.getNgayCapNhat());
        return dto;
    }

    public static KhachHang toEntity(KhachHangDTO dto) {
        if (dto == null) return null;
        KhachHang kh = new KhachHang();
        kh.setId(dto.getId());
        kh.setMaKhachHang(dto.getMaKhachHang());
        kh.setHoTen(dto.getHoTen());
        kh.setEmail(dto.getEmail());
        kh.setMatKhau(dto.getMatKhau());
        kh.setSoDienThoai(dto.getSoDienThoai());
        kh.setGioiTinh(dto.getGioiTinh());
        kh.setNgaySinh(dto.getNgaySinh());
        kh.setAvatar(dto.getAvatar());
        kh.setTrangThai(dto.getTrangThai());
        kh.setDaXacThuc(dto.getDaXacThuc());
        kh.setNgayTao(dto.getNgayTao());
        kh.setNgayCapNhat(dto.getNgayCapNhat());
        return kh;
    }
}
