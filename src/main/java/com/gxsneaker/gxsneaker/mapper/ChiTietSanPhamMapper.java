package com.gxsneaker.gxsneaker.mapper;

import com.gxsneaker.gxsneaker.dto.ChiTietSanPhamDTO;
import com.gxsneaker.gxsneaker.entity.ChiTietSanPham;

public class ChiTietSanPhamMapper {

    public static ChiTietSanPhamDTO toDTO(ChiTietSanPham ctsp) {
        if (ctsp == null) {
            return null;
        }

        ChiTietSanPhamDTO dto = new ChiTietSanPhamDTO();
        dto.setId(ctsp.getId());
        dto.setIdSanPham(ctsp.getSanPham() != null ? ctsp.getSanPham().getId() : null);
        dto.setIdMauSac(ctsp.getMauSac() != null ? ctsp.getMauSac().getId() : null);
        dto.setIdKichThuoc(ctsp.getKichThuoc() != null ? ctsp.getKichThuoc().getId() : null);
        dto.setMaChiTiet(ctsp.getMaChiTiet());
        dto.setSoLuongTon(ctsp.getSoLuongTon());
        dto.setGiaNhap(ctsp.getGiaNhap());
        dto.setGiaBan(ctsp.getGiaBan());
        dto.setTrangThai(ctsp.getTrangThai());

        if (ctsp.getSanPham() != null) {
            dto.setTenSanPham(ctsp.getSanPham().getTenSanPham());
            dto.setHinhAnh(ctsp.getSanPham().getAnhDaiDien());
        }
        if (ctsp.getMauSac() != null) {
            dto.setTenMauSac(ctsp.getMauSac().getTen());
        }
        if (ctsp.getKichThuoc() != null) {
            dto.setSize(ctsp.getKichThuoc().getSize());
        }

        return dto;
    }
}