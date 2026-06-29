package com.gxsneaker.gxsneaker.service.impl;

import com.gxsneaker.gxsneaker.dto.PhieuGiamGiaDTO;
import com.gxsneaker.gxsneaker.entity.PhieuGiamGia;
import com.gxsneaker.gxsneaker.mapper.PhieuGiamGiaMapper;
import com.gxsneaker.gxsneaker.repository.PhieuGiamGiaRepository;
import com.gxsneaker.gxsneaker.service.PhieuGiamGiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PhieuGiamGiaServiceImpl implements PhieuGiamGiaService {

    @Autowired
    private PhieuGiamGiaRepository phieuGiamGiaRepository;

    @Override
    public List<PhieuGiamGiaDTO> getAllPhieuGiamGia() {
        return phieuGiamGiaRepository.findAll().stream()
                .map(PhieuGiamGiaMapper::toDTO)
                .toList();
    }

    @Override
    public Optional<PhieuGiamGiaDTO> getPhieuGiamGiaById(Integer id) {
        return phieuGiamGiaRepository.findById(id).map(PhieuGiamGiaMapper::toDTO);
    }

    @Override
    public Optional<PhieuGiamGiaDTO> getPhieuGiamGiaByMa(String maPhieu) {
        return phieuGiamGiaRepository.findAll().stream()
                .filter(p -> p.getMaPhieu() != null && p.getMaPhieu().equalsIgnoreCase(maPhieu))
                .findFirst()
                .map(PhieuGiamGiaMapper::toDTO);
    }

    private void validatePhieuGiamGia(PhieuGiamGia p, Integer excludeId) {
        if (p.getMaPhieu() == null || p.getMaPhieu().trim().isEmpty()) {
            throw new RuntimeException("Mã phiếu giảm giá không được để trống!");
        }
        String ma = p.getMaPhieu().trim().toUpperCase();
        p.setMaPhieu(ma);

        if (p.getTenPhieu() == null || p.getTenPhieu().trim().isEmpty()) {
            throw new RuntimeException("Tên phiếu giảm giá không được để trống!");
        }

        // Check uniqueness of maPhieu
        Optional<PhieuGiamGia> existing = phieuGiamGiaRepository.findByMaPhieu(ma);
        if (existing.isPresent()) {
            if (excludeId == null || !existing.get().getId().equals(excludeId)) {
                throw new RuntimeException("Mã phiếu giảm giá này đã tồn tại trong hệ thống!");
            }
        }

        if (p.getGiaTriGiam() == null || p.getGiaTriGiam().doubleValue() <= 0) {
            throw new RuntimeException("Giá trị giảm phải lớn hơn 0!");
        }

        if (p.getLoaiGiamGia() != null && p.getLoaiGiamGia()) { // percentage
            if (p.getGiaTriGiam().doubleValue() > 100) {
                throw new RuntimeException("Giá trị giảm theo phần trăm không được vượt quá 100%!");
            }
        }

        if (p.getGiaTriDonHangToiThieu() == null || p.getGiaTriDonHangToiThieu().doubleValue() < 0) {
            throw new RuntimeException("Giá trị đơn hàng tối thiểu không được nhỏ hơn 0!");
        }

        if (p.getLoaiGiamGia() != null && p.getLoaiGiamGia()) {
            if (p.getGiaTriGiamToiDa() == null || p.getGiaTriGiamToiDa().doubleValue() < 0) {
                throw new RuntimeException("Giá trị giảm tối đa không được nhỏ hơn 0!");
            }
        } else {
            p.setGiaTriGiamToiDa(java.math.BigDecimal.ZERO);
        }

        if (p.getSoLuong() == null || p.getSoLuong() < 0) {
            throw new RuntimeException("Số lượng phát hành không được nhỏ hơn 0!");
        }

        if (p.getNgayBatDau() == null) {
            throw new RuntimeException("Ngày bắt đầu không được để trống!");
        }

        if (p.getNgayKetThuc() == null) {
            throw new RuntimeException("Ngày kết thúc không được để trống!");
        }

        if (p.getNgayKetThuc().before(p.getNgayBatDau())) {
            throw new RuntimeException("Ngày kết thúc phải sau ngày bắt đầu!");
        }
    }

    @Override
    public PhieuGiamGiaDTO createPhieuGiamGia(PhieuGiamGiaDTO phieuGiamGiaDTO) {
        PhieuGiamGia phieuGiamGia = PhieuGiamGiaMapper.toEntity(phieuGiamGiaDTO);
        validatePhieuGiamGia(phieuGiamGia, null);
        PhieuGiamGia saved = phieuGiamGiaRepository.save(phieuGiamGia);
        return PhieuGiamGiaMapper.toDTO(saved);
    }

    @Override
    public PhieuGiamGiaDTO updatePhieuGiamGia(Integer id, PhieuGiamGiaDTO details) {
        PhieuGiamGia entityDetails = PhieuGiamGiaMapper.toEntity(details);
        validatePhieuGiamGia(entityDetails, id);
        return phieuGiamGiaRepository.findById(id).map(p -> {
            p.setMaPhieu(entityDetails.getMaPhieu());
            p.setTenPhieu(entityDetails.getTenPhieu());
            p.setLoaiGiamGia(entityDetails.getLoaiGiamGia());
            p.setGiaTriGiam(entityDetails.getGiaTriGiam());
            p.setGiaTriDonHangToiThieu(entityDetails.getGiaTriDonHangToiThieu());
            p.setGiaTriGiamToiDa(entityDetails.getGiaTriGiamToiDa());
            p.setSoLuong(entityDetails.getSoLuong());
            p.setNgayBatDau(entityDetails.getNgayBatDau());
            p.setNgayKetThuc(entityDetails.getNgayKetThuc());
            p.setTrangThai(entityDetails.getTrangThai());
            PhieuGiamGia saved = phieuGiamGiaRepository.save(p);
            return PhieuGiamGiaMapper.toDTO(saved);
        }).orElseThrow(() -> new RuntimeException("PhieuGiamGia not found with id " + id));
    }

    @Override
    public void deletePhieuGiamGia(Integer id) {
        phieuGiamGiaRepository.deleteById(id);
    }
}
