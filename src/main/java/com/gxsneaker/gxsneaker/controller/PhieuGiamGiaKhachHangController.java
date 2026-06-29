package com.gxsneaker.gxsneaker.controller;

import com.gxsneaker.gxsneaker.dto.PhieuGiamGiaKhachHangDTO;
import com.gxsneaker.gxsneaker.service.PhieuGiamGiaKhachHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/phieu-giam-gia-khach-hang")
@CrossOrigin("*")
public class PhieuGiamGiaKhachHangController {

    @Autowired
    private PhieuGiamGiaKhachHangService phieuGiamGiaKhachHangService;

    @GetMapping("/khach-hang/{khachHangId}")
    public List<PhieuGiamGiaKhachHangDTO> getVouchersByKhachHangId(@PathVariable Integer khachHangId) {
        return phieuGiamGiaKhachHangService.getVouchersByKhachHangId(khachHangId);
    }

    @PostMapping("/assign")
    public ResponseEntity<PhieuGiamGiaKhachHangDTO> assignVoucherToKhachHang(
            @RequestParam Integer khachHangId,
            @RequestParam Integer phieuGiamGiaId) {
        try {
            PhieuGiamGiaKhachHangDTO result = phieuGiamGiaKhachHangService.assignVoucherToKhachHang(khachHangId, phieuGiamGiaId);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}/use")
    public ResponseEntity<PhieuGiamGiaKhachHangDTO> useVoucher(@PathVariable Integer id) {
        try {
            PhieuGiamGiaKhachHangDTO updated = phieuGiamGiaKhachHangService.markAsUsed(id);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
