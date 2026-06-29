package com.gxsneaker.gxsneaker.config;

import com.gxsneaker.gxsneaker.entity.NhanVien;
import com.gxsneaker.gxsneaker.entity.PhanQuyen;
import com.gxsneaker.gxsneaker.repository.NhanVienRepository;
import com.gxsneaker.gxsneaker.repository.PhanQuyenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final PhanQuyenRepository phanQuyenRepository;
    private final NhanVienRepository nhanVienRepository;
    private final PasswordEncoder passwordEncoder;
    private final org.springframework.jdbc.core.JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        // Create ADMIN role if not exists
        PhanQuyen adminRole = phanQuyenRepository.findByMaQuyen("ADMIN")
                .orElseGet(() -> {
                    PhanQuyen role = PhanQuyen.builder()
                            .maQuyen("ADMIN")
                            .tenQuyen("Quản trị viên")
                            .moTa("Quyền tối cao hệ thống")
                            .trangThai(true)
                            .build();
                    log.info("Creating default ADMIN role...");
                    return phanQuyenRepository.save(role);
                });

        // Create STAFF role if not exists
        phanQuyenRepository.findByMaQuyen("STAFF")
                .orElseGet(() -> {
                    PhanQuyen role = PhanQuyen.builder()
                            .maQuyen("STAFF")
                            .tenQuyen("Nhân viên")
                            .moTa("Nhân viên bán hàng")
                            .trangThai(true)
                            .build();
                    log.info("Creating default STAFF role...");
                    return phanQuyenRepository.save(role);
                });

        // Create default Admin account if no employees exist
        if (nhanVienRepository.count() == 0) {
            NhanVien admin = NhanVien.builder()
                    .phanQuyen(adminRole)
                    .maNhanVien("NV001")
                    .hoTen("Admin GX Sneaker")
                    .email("admin@gmail.com")
                    .matKhau(passwordEncoder.encode("123"))
                    .soDienThoai("0987654321")
                    .trangThai(true)
                    .build();
            nhanVienRepository.save(admin);
            log.info("=================================================");
            log.info("CREATED DEFAULT ADMIN ACCOUNT:");
            log.info("Username / Email: admin@gmail.com");
            log.info("Password: 123");
            log.info("=================================================");
        }

        log.info("Starting safe database columns alter to NVARCHAR...");
        alterColumnSafely("dia_chi", "ho_ten_nguoi_nhan", "NVARCHAR(255)");
        alterColumnSafely("dia_chi", "tinh_thanh", "NVARCHAR(255)");
        alterColumnSafely("dia_chi", "phuong_xa", "NVARCHAR(255)");
        alterColumnSafely("dia_chi", "dia_chi_chi_tiet", "NVARCHAR(255)");
        alterColumnSafely("khach_hang", "ho_ten", "NVARCHAR(255)");
        alterColumnSafely("nhan_vien", "ho_ten", "NVARCHAR(255)");
        alterColumnSafely("phieu_giam_gia", "ten_phieu", "NVARCHAR(255)");
        alterColumnSafely("chat_lieu", "ten", "NVARCHAR(255)");
        alterColumnSafely("co_giay", "ten", "NVARCHAR(255)");
        alterColumnSafely("danh_muc", "ten", "NVARCHAR(255)");
        alterColumnSafely("de_giay", "ten", "NVARCHAR(255)");
        alterColumnSafely("don_vi_van_chuyen", "ten_cong_ty", "NVARCHAR(255)");
        alterColumnSafely("hoa_don", "ten_nguoi_nhan", "NVARCHAR(255)");
        alterColumnSafely("hoa_don", "ghi_chu", "NVARCHAR(500)");
        alterColumnSafely("hoa_don", "ly_do_huy", "NVARCHAR(500)");
        alterColumnSafely("mau_sac", "ten", "NVARCHAR(255)");
        alterColumnSafely("san_pham", "ten_san_pham", "NVARCHAR(255)");
        alterColumnSafely("san_pham", "mo_ta", "NVARCHAR(500)");
        alterColumnSafely("thuong_hieu", "ten", "NVARCHAR(255)");
        alterColumnSafely("thuong_hieu", "mo_ta", "NVARCHAR(500)");
        alterColumnSafely("xuat_xu", "ten", "NVARCHAR(255)");
        log.info("Finished safe database columns alter.");
    }

    private void alterColumnSafely(String tableName, String columnName, String definition) {
        try {
            jdbcTemplate.execute(String.format("ALTER TABLE %s ALTER COLUMN %s %s", tableName, columnName, definition));
        } catch (Exception e) {
            log.warn("Could not alter column {}.{} to {}: {}", tableName, columnName, definition, e.getMessage());
        }
    }
}
