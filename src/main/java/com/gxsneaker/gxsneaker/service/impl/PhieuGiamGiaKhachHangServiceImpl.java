package com.gxsneaker.gxsneaker.service.impl;

import com.gxsneaker.gxsneaker.dto.PhieuGiamGiaKhachHangDTO;
import com.gxsneaker.gxsneaker.entity.KhachHang;
import com.gxsneaker.gxsneaker.entity.PhieuGiamGia;
import com.gxsneaker.gxsneaker.entity.PhieuGiamGiaKhachHang;
import com.gxsneaker.gxsneaker.mapper.PhieuGiamGiaKhachHangMapper;
import com.gxsneaker.gxsneaker.repository.KhachHangRepository;
import com.gxsneaker.gxsneaker.repository.PhieuGiamGiaKhachHangRepository;
import com.gxsneaker.gxsneaker.repository.PhieuGiamGiaRepository;
import com.gxsneaker.gxsneaker.service.PhieuGiamGiaKhachHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PhieuGiamGiaKhachHangServiceImpl implements PhieuGiamGiaKhachHangService {

    @Autowired
    private PhieuGiamGiaKhachHangRepository phieuGiamGiaKhachHangRepository;

    @Autowired
    private KhachHangRepository khachHangRepository;

    @Autowired
    private PhieuGiamGiaRepository phieuGiamGiaRepository;

    @Override
    public List<PhieuGiamGiaKhachHangDTO> getVouchersByKhachHangId(Integer khachHangId) {
        return phieuGiamGiaKhachHangRepository.findAll().stream()
                .filter(p -> p.getKhachHang() != null && p.getKhachHang().getId().equals(khachHangId))
                .map(PhieuGiamGiaKhachHangMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PhieuGiamGiaKhachHangDTO assignVoucherToKhachHang(Integer khachHangId, Integer phieuGiamGiaId) {
        KhachHang khachHang = khachHangRepository.findById(khachHangId)
                .orElseThrow(() -> new RuntimeException("KhachHang not found with id " + khachHangId));

        PhieuGiamGia phieuGiamGia = phieuGiamGiaRepository.findById(phieuGiamGiaId)
                .orElseThrow(() -> new RuntimeException("PhieuGiamGia not found with id " + phieuGiamGiaId));

        PhieuGiamGiaKhachHang pggh = PhieuGiamGiaKhachHang.builder()
                .khachHang(khachHang)
                .phieuGiamGia(phieuGiamGia)
                .daSuDung(false)
                .build();

        PhieuGiamGiaKhachHang saved = phieuGiamGiaKhachHangRepository.save(pggh);
        return PhieuGiamGiaKhachHangMapper.toDTO(saved);
    }

    @Override
    public PhieuGiamGiaKhachHangDTO markAsUsed(Integer id) {
        return phieuGiamGiaKhachHangRepository.findById(id).map(p -> {
            p.setDaSuDung(true);
            PhieuGiamGiaKhachHang saved = phieuGiamGiaKhachHangRepository.save(p);
            return PhieuGiamGiaKhachHangMapper.toDTO(saved);
        }).orElseThrow(() -> new RuntimeException("PhieuGiamGiaKhachHang not found with id " + id));
    }

    @Override
    public Optional<PhieuGiamGiaKhachHangDTO> getById(Integer id) {
        return phieuGiamGiaKhachHangRepository.findById(id).map(PhieuGiamGiaKhachHangMapper::toDTO);
    }
}
