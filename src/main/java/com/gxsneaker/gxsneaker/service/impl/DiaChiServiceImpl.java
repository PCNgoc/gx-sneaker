package com.gxsneaker.gxsneaker.service.impl;

import com.gxsneaker.gxsneaker.dto.DiaChiDTO;
import com.gxsneaker.gxsneaker.entity.DiaChi;
import com.gxsneaker.gxsneaker.entity.KhachHang;
import com.gxsneaker.gxsneaker.mapper.DiaChiMapper;
import com.gxsneaker.gxsneaker.repository.DiaChiRepository;
import com.gxsneaker.gxsneaker.repository.KhachHangRepository;
import com.gxsneaker.gxsneaker.service.DiaChiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DiaChiServiceImpl implements DiaChiService {

    @Autowired
    private DiaChiRepository diaChiRepository;

    @Autowired
    private KhachHangRepository khachHangRepository;

    @Override
    public List<DiaChiDTO> getDiaChiByKhachHangId(Integer khachHangId) {
        return diaChiRepository.findAll().stream()
                .filter(dc -> dc.getKhachHang() != null && dc.getKhachHang().getId().equals(khachHangId))
                .map(DiaChiMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<DiaChiDTO> getDiaChiById(Integer id) {
        return diaChiRepository.findById(id).map(DiaChiMapper::toDTO);
    }

    @Override
    public DiaChiDTO createDiaChi(DiaChiDTO diaChiDTO) {
        DiaChi diaChi = DiaChiMapper.toEntity(diaChiDTO);
        if (diaChiDTO.getIdKhachHang() != null) {
            KhachHang kh = khachHangRepository.findById(diaChiDTO.getIdKhachHang())
                    .orElseThrow(() -> new RuntimeException("KhachHang not found with id " + diaChiDTO.getIdKhachHang()));
            diaChi.setKhachHang(kh);
        }
        DiaChi saved = diaChiRepository.save(diaChi);
        return DiaChiMapper.toDTO(saved);
    }

    @Override
    public DiaChiDTO updateDiaChi(Integer id, DiaChiDTO diaChiDetails) {
        return diaChiRepository.findById(id).map(diaChi -> {
            diaChi.setHoTenNguoiNhan(diaChiDetails.getHoTenNguoiNhan());
            diaChi.setSoDienThoai(diaChiDetails.getSoDienThoai());
            diaChi.setTinhThanh(diaChiDetails.getTinhThanh());
            diaChi.setQuanHuyen(diaChiDetails.getQuanHuyen());
            diaChi.setPhuongXa(diaChiDetails.getPhuongXa());
            diaChi.setDiaChiChiTiet(diaChiDetails.getDiaChiChiTiet());
            DiaChi saved = diaChiRepository.save(diaChi);
            return DiaChiMapper.toDTO(saved);
        }).orElseThrow(() -> new RuntimeException("DiaChi not found with id " + id));
    }

    @Override
    public void deleteDiaChi(Integer id) {
        diaChiRepository.deleteById(id);
    }
}
