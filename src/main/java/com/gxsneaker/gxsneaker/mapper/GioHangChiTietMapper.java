package com.gxsneaker.gxsneaker.mapper;

import com.gxsneaker.gxsneaker.dto.GioHangChiTietDTO;
import com.gxsneaker.gxsneaker.entity.ChiTietSanPham;
import com.gxsneaker.gxsneaker.entity.GioHang;
import com.gxsneaker.gxsneaker.entity.GioHangChiTiet;

public class GioHangChiTietMapper {

    public static GioHangChiTietDTO toDTO(GioHangChiTiet item) {
        if (item == null) return null;
        GioHangChiTietDTO dto = new GioHangChiTietDTO();
        dto.setId(item.getId());
        if (item.getGioHang() != null) {
            dto.setIdGioHang(item.getGioHang().getId());
        }
        if (item.getChiTietSanPham() != null) {
            dto.setIdChiTietSanPham(item.getChiTietSanPham().getId());
            dto.setChiTietSanPham(ChiTietSanPhamMapper.toDTO(item.getChiTietSanPham()));
        }
        dto.setSoLuong(item.getSoLuong());
        dto.setNgayThem(item.getNgayThem());
        return dto;
    }

    public static GioHangChiTiet toEntity(GioHangChiTietDTO dto) {
        if (dto == null) return null;
        GioHangChiTiet item = new GioHangChiTiet();
        item.setId(dto.getId());
        if (dto.getIdGioHang() != null) {
            GioHang gh = new GioHang();
            gh.setId(dto.getIdGioHang());
            item.setGioHang(gh);
        }
        if (dto.getIdChiTietSanPham() != null) {
            ChiTietSanPham ctsp = new ChiTietSanPham();
            ctsp.setId(dto.getIdChiTietSanPham());
            item.setChiTietSanPham(ctsp);
        }
        item.setSoLuong(dto.getSoLuong());
        item.setNgayThem(dto.getNgayThem());
        return item;
    }
}
