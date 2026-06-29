package com.gxsneaker.gxsneaker.service.impl;

import com.gxsneaker.gxsneaker.dto.KhachHangDTO;
import com.gxsneaker.gxsneaker.entity.KhachHang;
import com.gxsneaker.gxsneaker.mapper.KhachHangMapper;
import com.gxsneaker.gxsneaker.repository.KhachHangRepository;
import com.gxsneaker.gxsneaker.service.KhachHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class KhachHangServiceImpl implements KhachHangService {

    @Autowired
    private KhachHangRepository khachHangRepository;

    @Autowired
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @Override
    public List<KhachHangDTO> getAllKhachHang() {
        return khachHangRepository.findAll().stream()
                .map(KhachHangMapper::toDTO)
                .toList();
    }

    @Override
    public Optional<KhachHangDTO> getKhachHangById(Integer id) {
        return khachHangRepository.findById(id).map(KhachHangMapper::toDTO);
    }

    @Override
    public KhachHangDTO createKhachHang(KhachHangDTO khachHangDTO) {
        if (khachHangDTO.getEmail() != null && !khachHangDTO.getEmail().trim().isEmpty()) {
            if (khachHangRepository.findByEmail(khachHangDTO.getEmail().trim()).isPresent()) {
                throw new RuntimeException("Email này đã được sử dụng bởi khách hàng khác!");
            }
        }
        if (khachHangDTO.getSoDienThoai() != null && !khachHangDTO.getSoDienThoai().trim().isEmpty()) {
            if (khachHangRepository.findBySoDienThoai(khachHangDTO.getSoDienThoai().trim()).isPresent()) {
                throw new RuntimeException("Số điện thoại này đã được sử dụng bởi khách hàng khác!");
            }
        }

        KhachHang khachHang = KhachHangMapper.toEntity(khachHangDTO);

        long count = khachHangRepository.count() + 1;
        khachHang.setMaKhachHang(String.format("KH%03d", count));
        if (khachHang.getMatKhau() == null || khachHang.getMatKhau().trim().isEmpty()) {
            khachHang.setMatKhau(passwordEncoder.encode("123456"));
        }
        if (khachHang.getTrangThai() == null) {
            khachHang.setTrangThai(true);
        }
        if (khachHang.getDaXacThuc() == null) {
            khachHang.setDaXacThuc(true);
        }
        KhachHang saved = khachHangRepository.save(khachHang);
        return KhachHangMapper.toDTO(saved);
    }

    @Override
    public KhachHangDTO updateKhachHang(Integer id, KhachHangDTO khachHangDetails) {
        return khachHangRepository.findById(id).map(khachHang -> {
            if (khachHangDetails.getEmail() != null && !khachHangDetails.getEmail().trim().isEmpty()) {
                Optional<KhachHang> existingEmail = khachHangRepository.findByEmail(khachHangDetails.getEmail().trim());
                if (existingEmail.isPresent() && !existingEmail.get().getId().equals(id)) {
                    throw new RuntimeException("Email này đã được sử dụng bởi khách hàng khác!");
                }
            }
            if (khachHangDetails.getSoDienThoai() != null && !khachHangDetails.getSoDienThoai().trim().isEmpty()) {
                Optional<KhachHang> existingPhone = khachHangRepository.findBySoDienThoai(khachHangDetails.getSoDienThoai().trim());
                if (existingPhone.isPresent() && !existingPhone.get().getId().equals(id)) {
                    throw new RuntimeException("Số điện thoại này đã được sử dụng bởi khách hàng khác!");
                }
            }

            khachHang.setMaKhachHang(khachHangDetails.getMaKhachHang());
            khachHang.setHoTen(khachHangDetails.getHoTen());
            khachHang.setEmail(khachHangDetails.getEmail());
            khachHang.setSoDienThoai(khachHangDetails.getSoDienThoai());
            khachHang.setGioiTinh(khachHangDetails.getGioiTinh());
            khachHang.setNgaySinh(khachHangDetails.getNgaySinh());
            khachHang.setTrangThai(khachHangDetails.getTrangThai());
            KhachHang saved = khachHangRepository.save(khachHang);
            return KhachHangMapper.toDTO(saved);
        }).orElseThrow(() -> new RuntimeException("KhachHang not found with id " + id));
    }

    @Override
    public void deleteKhachHang(Integer id) {
        khachHangRepository.deleteById(id);
    }
}
