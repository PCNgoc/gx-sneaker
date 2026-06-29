package com.gxsneaker.gxsneaker.mapper;

import com.gxsneaker.gxsneaker.dto.DiaChiDTO;
import com.gxsneaker.gxsneaker.entity.DiaChi;
import com.gxsneaker.gxsneaker.entity.KhachHang;

public class DiaChiMapper {

    public static DiaChiDTO toDTO(DiaChi dc) {
        if (dc == null) return null;
        DiaChiDTO dto = new DiaChiDTO();
        dto.setId(dc.getId());
        if (dc.getKhachHang() != null) {
            dto.setIdKhachHang(dc.getKhachHang().getId());
        }
        dto.setHoTenNguoiNhan(dc.getHoTenNguoiNhan());
        dto.setSoDienThoai(dc.getSoDienThoai());
        dto.setTinhThanh(dc.getTinhThanh());
        dto.setQuanHuyen(dc.getQuanHuyen());
        dto.setPhuongXa(dc.getPhuongXa());
        dto.setDiaChiChiTiet(dc.getDiaChiChiTiet());
        dto.setNgayTao(dc.getNgayTao());
        dto.setNgayCapNhat(dc.getNgayCapNhat());
        return dto;
    }

    public static DiaChi toEntity(DiaChiDTO dto) {
        if (dto == null) return null;
        DiaChi dc = new DiaChi();
        dc.setId(dto.getId());
        if (dto.getIdKhachHang() != null) {
            KhachHang kh = new KhachHang();
            kh.setId(dto.getIdKhachHang());
            dc.setKhachHang(kh);
        }
        dc.setHoTenNguoiNhan(dto.getHoTenNguoiNhan());
        dc.setSoDienThoai(dto.getSoDienThoai());
        dc.setTinhThanh(dto.getTinhThanh());
        dc.setQuanHuyen(dto.getQuanHuyen());
        dc.setPhuongXa(dto.getPhuongXa());
        dc.setDiaChiChiTiet(dto.getDiaChiChiTiet());
        dc.setNgayTao(dto.getNgayTao());
        dc.setNgayCapNhat(dto.getNgayCapNhat());
        return dc;
    }
}
