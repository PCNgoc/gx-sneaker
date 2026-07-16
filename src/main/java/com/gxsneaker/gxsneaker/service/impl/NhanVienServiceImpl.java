package com.gxsneaker.gxsneaker.service.impl;

import com.gxsneaker.gxsneaker.dto.NhanVienDTO;
import com.gxsneaker.gxsneaker.entity.NhanVien;
import com.gxsneaker.gxsneaker.entity.PhanQuyen;
import com.gxsneaker.gxsneaker.repository.NhanVienRepository;
import com.gxsneaker.gxsneaker.repository.PhanQuyenRepository;
import com.gxsneaker.gxsneaker.service.NhanVienService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NhanVienServiceImpl implements NhanVienService {

    private final NhanVienRepository nhanVienRepository;

    private final PhanQuyenRepository phanQuyenRepository;

    private final PasswordEncoder passwordEncoder;

    // ================= DTO =================

    private NhanVienDTO convertToDTO(NhanVien nv){

        return NhanVienDTO.builder()

                .id(nv.getId())

                .idPhanQuyen(
                        nv.getPhanQuyen()!=null?
                                nv.getPhanQuyen().getId():null
                )

                .tenQuyen(
                        nv.getPhanQuyen()!=null?
                                nv.getPhanQuyen().getTenQuyen():null
                )

                .maNhanVien(nv.getMaNhanVien())

                .hoTen(nv.getHoTen())

                .email(nv.getEmail())

                .soDienThoai(nv.getSoDienThoai())

                .gioiTinh(nv.getGioiTinh())

                .diaChi(nv.getDiaChi())

                .anhDaiDien(nv.getAnhDaiDien())

                .trangThai(nv.getTrangThai())

                .build();

    }

    // ================= AUTO CODE =================

    private String taoMaNhanVien(){

        Optional<NhanVien> last =
                nhanVienRepository.findTopByOrderByIdDesc();

        if(last.isEmpty()){

            return "NV001";

        }

        String ma =
                last.get().getMaNhanVien().replace("NV","");

        int number =
                Integer.parseInt(ma);

        return String.format("NV%03d",number+1);

    }

    // ================= GET ALL =================

    @Override
    public List<NhanVienDTO> getAll() {

        return nhanVienRepository.findAll()

                .stream()

                .map(this::convertToDTO)

                .collect(Collectors.toList());

    }

    // ================= GET ONE =================

    @Override
    public NhanVienDTO getById(Integer id) {

        NhanVien nv =
                nhanVienRepository.findById(id)

                        .orElseThrow(
                                ()->new RuntimeException("Không tìm thấy nhân viên")
                        );

        return convertToDTO(nv);

    }

    // ================= CREATE =================

    @Override
    @Transactional
    public NhanVienDTO create(NhanVienDTO dto) {

        if(nhanVienRepository.existsByEmail(dto.getEmail())){

            throw new RuntimeException("Email đã tồn tại");

        }

        if(nhanVienRepository.existsBySoDienThoai(dto.getSoDienThoai())){

            throw new RuntimeException("Số điện thoại đã tồn tại");

        }

        PhanQuyen pq =
                phanQuyenRepository.findById(dto.getIdPhanQuyen())

                        .orElseThrow(
                                ()->new RuntimeException("Không tìm thấy quyền")
                        );

        NhanVien nv = new NhanVien();

        nv.setPhanQuyen(pq);

        nv.setMaNhanVien(
                taoMaNhanVien()
        );

        nv.setHoTen(dto.getHoTen());

        nv.setEmail(dto.getEmail());

        nv.setMatKhau(

                passwordEncoder.encode(dto.getMatKhau())

        );

        nv.setSoDienThoai(dto.getSoDienThoai());

        nv.setGioiTinh(dto.getGioiTinh());

        nv.setDiaChi(dto.getDiaChi());

        nv.setAnhDaiDien(dto.getAnhDaiDien());

        nv.setTrangThai(true);

        nv.setNgayTao(LocalDateTime.now());

        nhanVienRepository.save(nv);

        return convertToDTO(nv);

    }

    // ================= UPDATE =================

    @Override
    @Transactional
    public NhanVienDTO update(Integer id, NhanVienDTO dto) {

        NhanVien nv =
                nhanVienRepository.findById(id)

                        .orElseThrow(
                                ()->new RuntimeException("Không tìm thấy nhân viên")
                        );

        if(
                nhanVienRepository.existsByEmailAndIdNot(
                        dto.getEmail(),
                        id
                )
        ){

            throw new RuntimeException("Email đã tồn tại");

        }

        if(
                nhanVienRepository.existsBySoDienThoaiAndIdNot(
                        dto.getSoDienThoai(),
                        id
                )
        ){

            throw new RuntimeException("Số điện thoại đã tồn tại");

        }

        PhanQuyen pq =
                phanQuyenRepository.findById(dto.getIdPhanQuyen())

                        .orElseThrow(
                                ()->new RuntimeException("Không tìm thấy quyền")
                        );

        nv.setPhanQuyen(pq);

        nv.setHoTen(dto.getHoTen());

        nv.setEmail(dto.getEmail());

        if (dto.getMatKhau() != null && !dto.getMatKhau().isBlank()) {
            nv.setMatKhau(passwordEncoder.encode(dto.getMatKhau()));
        }

        nv.setSoDienThoai(dto.getSoDienThoai());

        nv.setGioiTinh(dto.getGioiTinh());

        nv.setDiaChi(dto.getDiaChi());

        nv.setAnhDaiDien(dto.getAnhDaiDien());

        nv.setTrangThai(dto.getTrangThai());

        nv.setNgayCapNhat(LocalDateTime.now());

        nhanVienRepository.save(nv);

        return convertToDTO(nv);

    }

    // ================= DELETE =================

    @Override
    @Transactional
    public void delete(Integer id) {

        NhanVien nv =
                nhanVienRepository.findById(id)

                        .orElseThrow(
                                ()->new RuntimeException("Không tìm thấy nhân viên")
                        );

        // Soft Delete

        nv.setTrangThai(false);

        nv.setNgayCapNhat(LocalDateTime.now());

        nhanVienRepository.save(nv);

    }

}