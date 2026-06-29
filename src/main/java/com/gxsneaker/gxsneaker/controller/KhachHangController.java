package com.gxsneaker.gxsneaker.controller;

import com.gxsneaker.gxsneaker.dto.KhachHangDTO;
import com.gxsneaker.gxsneaker.service.KhachHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/khach-hang")
@CrossOrigin("*")
public class KhachHangController {

    @Autowired
    private KhachHangService khachHangService;

    @GetMapping
    public List<KhachHangDTO> getAllKhachHang() {
        return khachHangService.getAllKhachHang();
    }

    @GetMapping("/{id}")
    public ResponseEntity<KhachHangDTO> getKhachHangById(@PathVariable Integer id) {
        return khachHangService.getKhachHangById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createKhachHang(@RequestBody KhachHangDTO khachHangDTO) {
        try {
            KhachHangDTO created = khachHangService.createKhachHang(khachHangDTO);
            return ResponseEntity.ok(created);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(java.util.Map.of("message", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateKhachHang(@PathVariable Integer id, @RequestBody KhachHangDTO khachHangDTO) {
        try {
            KhachHangDTO updatedKhachHang = khachHangService.updateKhachHang(id, khachHangDTO);
            return ResponseEntity.ok(updatedKhachHang);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.badRequest().body(java.util.Map.of("message", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteKhachHang(@PathVariable Integer id) {
        khachHangService.deleteKhachHang(id);
        return ResponseEntity.noContent().build();
    }
}
