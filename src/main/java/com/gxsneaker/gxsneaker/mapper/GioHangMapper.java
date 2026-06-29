package com.gxsneaker.gxsneaker.mapper;

import com.gxsneaker.gxsneaker.dto.GioHangDTO;
import com.gxsneaker.gxsneaker.entity.GioHang;
import com.gxsneaker.gxsneaker.entity.KhachHang;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class GioHangMapper {

    public static GioHangDTO toDTO(GioHang gh) {
        if (gh == null) return null;
        GioHangDTO dto = new GioHangDTO();
        dto.setId(gh.getId());
        if (gh.getKhachHang() != null) {
            dto.setIdKhachHang(gh.getKhachHang().getId());
        }
        dto.setTongSoLuong(gh.getTongSoLuong());
        dto.setNgayTao(gh.getNgayTao());
        dto.setNgayCapNhat(gh.getNgayCapNhat());
        if (gh.getGioHangChiTiets() != null) {
            dto.setItems(gh.getGioHangChiTiets().stream()
                    .map(GioHangChiTietMapper::toDTO)
                    .collect(Collectors.toList()));
        } else {
            dto.setItems(new ArrayList<>());
        }
        return dto;
    }

    public static GioHang toEntity(GioHangDTO dto) {
        if (dto == null) return null;
        GioHang gh = new GioHang();
        gh.setId(dto.getId());
        if (dto.getIdKhachHang() != null) {
            KhachHang kh = new KhachHang();
            kh.setId(dto.getIdKhachHang());
            gh.setKhachHang(kh);
        }
        gh.setTongSoLuong(dto.getTongSoLuong());
        gh.setNgayTao(dto.getNgayTao());
        gh.setNgayCapNhat(dto.getNgayCapNhat());
        if (dto.getItems() != null) {
            gh.setGioHangChiTiets(dto.getItems().stream()
                    .map(GioHangChiTietMapper::toEntity)
                    .collect(Collectors.toList()));
        } else {
            gh.setGioHangChiTiets(new ArrayList<>());
        }
        return gh;
    }
}
