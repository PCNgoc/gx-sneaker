package com.gxsneaker.gxsneaker.repository;

import com.gxsneaker.gxsneaker.entity.NhanVien;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NhanVienRepository extends JpaRepository<NhanVien, Integer> {

    Optional<NhanVien> findByEmail(String email);

    Optional<NhanVien> findBySoDienThoai(String soDienThoai);

    boolean existsByEmail(String email);

    boolean existsBySoDienThoai(String soDienThoai);

    boolean existsByMaNhanVien(String maNhanVien);

    boolean existsByEmailAndIdNot(String email, Integer id);

    boolean existsBySoDienThoaiAndIdNot(String soDienThoai, Integer id);

    Optional<NhanVien> findTopByOrderByIdDesc();

    Page<NhanVien> findByHoTenContainingIgnoreCase(String keyword, Pageable pageable);

}