package com.gxsneaker.gxsneaker.controller;

import com.gxsneaker.gxsneaker.dto.NhanVienDTO;
import com.gxsneaker.gxsneaker.service.NhanVienService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nhan-vien")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class NhanVienController {

    private final NhanVienService nhanVienService;

    // ================= GET ALL =================

    @GetMapping
    public ResponseEntity<List<NhanVienDTO>> getAll() {

        return ResponseEntity.ok(
                nhanVienService.getAll()
        );

    }

    // ================= GET BY ID =================

    @GetMapping("/{id}")
    public ResponseEntity<NhanVienDTO> getById(
            @PathVariable Integer id
    ) {

        return ResponseEntity.ok(

                nhanVienService.getById(id)

        );

    }

    // ================= CREATE =================

    @PostMapping
    public ResponseEntity<NhanVienDTO> create(

            @RequestBody NhanVienDTO dto

    ) {

        return ResponseEntity.status(HttpStatus.CREATED)

                .body(

                        nhanVienService.create(dto)

                );

    }

    // ================= UPDATE =================

    @PutMapping("/{id}")
    public ResponseEntity<NhanVienDTO> update(

            @PathVariable Integer id,

            @RequestBody NhanVienDTO dto

    ) {

        return ResponseEntity.ok(

                nhanVienService.update(id, dto)

        );

    }

    // ================= DELETE =================

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(

            @PathVariable Integer id

    ) {

        nhanVienService.delete(id);

        return ResponseEntity.ok(

                "Xóa nhân viên thành công"

        );

    }

}