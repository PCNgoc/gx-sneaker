package com.gxsneaker.gxsneaker.repository;

import com.gxsneaker.gxsneaker.entity.PhieuGiamGia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PhieuGiamGiaRepository extends JpaRepository<PhieuGiamGia, Integer> {

    Optional<PhieuGiamGia> findByMaPhieu(String maPhieu);

    Optional<PhieuGiamGia> findByMaPhieuIgnoreCase(String maPhieu);
}