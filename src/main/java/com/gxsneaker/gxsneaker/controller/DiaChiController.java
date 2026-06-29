package com.gxsneaker.gxsneaker.controller;

import com.gxsneaker.gxsneaker.dto.DiaChiDTO;
import com.gxsneaker.gxsneaker.service.DiaChiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dia-chi")
@CrossOrigin("*")
public class DiaChiController {

    @Autowired
    private DiaChiService diaChiService;

    @GetMapping("/khach-hang/{khachHangId}")
    public List<DiaChiDTO> getDiaChiByKhachHangId(@PathVariable Integer khachHangId) {
        return diaChiService.getDiaChiByKhachHangId(khachHangId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DiaChiDTO> getDiaChiById(@PathVariable Integer id) {
        return diaChiService.getDiaChiById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public DiaChiDTO createDiaChi(@RequestBody DiaChiDTO diaChi) {
        return diaChiService.createDiaChi(diaChi);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DiaChiDTO> updateDiaChi(@PathVariable Integer id, @RequestBody DiaChiDTO diaChi) {
        try {
            DiaChiDTO updated = diaChiService.updateDiaChi(id, diaChi);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDiaChi(@PathVariable Integer id) {
        diaChiService.deleteDiaChi(id);
        return ResponseEntity.noContent().build();
    }
}
