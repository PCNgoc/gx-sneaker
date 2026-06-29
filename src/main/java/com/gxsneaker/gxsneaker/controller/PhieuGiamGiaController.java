package com.gxsneaker.gxsneaker.controller;

import com.gxsneaker.gxsneaker.dto.PhieuGiamGiaDTO;
import com.gxsneaker.gxsneaker.service.PhieuGiamGiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/phieu-giam-gia")
@CrossOrigin("*")
public class PhieuGiamGiaController {

    @Autowired
    private PhieuGiamGiaService phieuGiamGiaService;

    @GetMapping
    public List<PhieuGiamGiaDTO> getAllPhieuGiamGia() {
        return phieuGiamGiaService.getAllPhieuGiamGia();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PhieuGiamGiaDTO> getPhieuGiamGiaById(@PathVariable Integer id) {
        return phieuGiamGiaService.getPhieuGiamGiaById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/ma/{maPhieu}")
    public ResponseEntity<PhieuGiamGiaDTO> getPhieuGiamGiaByMa(@PathVariable String maPhieu) {
        return phieuGiamGiaService.getPhieuGiamGiaByMa(maPhieu)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createPhieuGiamGia(@RequestBody PhieuGiamGiaDTO phieuGiamGia) {
        try {
            PhieuGiamGiaDTO created = phieuGiamGiaService.createPhieuGiamGia(phieuGiamGia);
            return ResponseEntity.ok(created);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(java.util.Map.of("message", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePhieuGiamGia(@PathVariable Integer id, @RequestBody PhieuGiamGiaDTO phieuGiamGia) {
        try {
            PhieuGiamGiaDTO updated = phieuGiamGiaService.updatePhieuGiamGia(id, phieuGiamGia);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.badRequest().body(java.util.Map.of("message", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePhieuGiamGia(@PathVariable Integer id) {
        phieuGiamGiaService.deletePhieuGiamGia(id);
        return ResponseEntity.noContent().build();
    }
}
