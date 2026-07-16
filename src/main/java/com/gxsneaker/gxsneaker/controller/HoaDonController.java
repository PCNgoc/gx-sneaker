package com.gxsneaker.gxsneaker.controller;

import com.gxsneaker.gxsneaker.dto.*;
import com.gxsneaker.gxsneaker.entity.HoaDon;
import com.gxsneaker.gxsneaker.entity.LichSuTrangThaiHoaDon;
import com.gxsneaker.gxsneaker.entity.PhieuGiamGia;
import com.gxsneaker.gxsneaker.repository.HoaDonRepository;
import com.gxsneaker.gxsneaker.repository.LichSuTrangThaiHoaDonRepository;
import com.gxsneaker.gxsneaker.repository.PhieuGiamGiaRepository;
import com.gxsneaker.gxsneaker.service.HoaDonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/hoa-don")
@CrossOrigin("*")
public class HoaDonController {

    @Autowired
    private PhieuGiamGiaRepository phieuGiamGiaRepository;

    @Autowired
    private HoaDonRepository repository;

    @Autowired
    private LichSuTrangThaiHoaDonRepository lichSuRepository;

    @Autowired
    private HoaDonService hoaDonService;

    @GetMapping
    public List<HoaDon> getAll() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public OrderResponseDTO getById(@PathVariable Long id) {
        return hoaDonService.getOrderById(id);
    }

    @PostMapping
    public HoaDon create(@RequestBody HoaDon hoaDon) {
        return repository.save(hoaDon);
    }

    @PutMapping("/{id}")
    public HoaDon update(
            @PathVariable Long id,
            @RequestBody HoaDon hoaDon
    ) {
        hoaDon.setId(id);
        return repository.save(hoaDon);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repository.deleteById(id);
    }

    @GetMapping("/search")
    public List<HoaDon> search(
            @RequestParam(required = false) String maHoaDon,
            @RequestParam(required = false) String trangThai
    ) {
        if ((maHoaDon == null || maHoaDon.isBlank())
                && (trangThai == null || trangThai.isBlank())) {
            return repository.findAll();
        }

        if (trangThai == null || trangThai.isBlank()) {
            return repository.findByMaHoaDonContaining(maHoaDon);
        }

        if (maHoaDon == null || maHoaDon.isBlank()) {
            return repository.findByTrangThai(trangThai);
        }

        return repository.findByMaHoaDonContainingAndTrangThai(
                maHoaDon,
                trangThai
        );
    }

    @GetMapping("/page")
    public Page<HoaDon> getPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return repository.findAll(PageRequest.of(page, size));
    }

    @GetMapping("/filter-date")
    public List<HoaDon> filterDate(
            @RequestParam
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            Date from,

            @RequestParam
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            Date to
    ) {
        return repository.findByNgayDatHangBetween(from, to);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(
            @PathVariable Long id,
            @RequestBody UpdateTrangThaiRequest request
    ) {
        Optional<HoaDon> optionalHoaDon = repository.findById(id);

        if (optionalHoaDon.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body("Không tìm thấy hóa đơn");
        }

        HoaDon hoaDon = optionalHoaDon.get();

        String trangThaiCu = hoaDon.getTrangThai();

        if (trangThaiCu.equalsIgnoreCase(request.getTrangThaiMoi())) {
            return ResponseEntity
                    .badRequest()
                    .body("Trạng thái mới phải khác trạng thái hiện tại");
        }

        hoaDon.setTrangThai(request.getTrangThaiMoi());

        repository.save(hoaDon);

        LichSuTrangThaiHoaDon lichSu = new LichSuTrangThaiHoaDon();

        lichSu.setIdHoaDon(id);
        lichSu.setTrangThaiCu(trangThaiCu);
        lichSu.setTrangThaiMoi(request.getTrangThaiMoi());
        lichSu.setNguoiThucHien(request.getNguoiThucHien());
        lichSu.setThoiGian(new Date());
        lichSu.setGhiChu(request.getGhiChu());

        lichSuRepository.save(lichSu);

        return ResponseEntity.ok("Cập nhật trạng thái thành công");
    }

    @GetMapping("/{id}/history")
    public List<LichSuTrangThaiHoaDon> getHistory(
            @PathVariable Long id
    ) {
        return lichSuRepository.findByIdHoaDonOrderByThoiGianAsc(id);
    }

    @GetMapping("/thong-ke/tong-doanh-thu")
    public ResponseEntity<BigDecimal> getTongDoanhThu() {
        return ResponseEntity.ok(repository.getTongDoanhThu());
    }

    @GetMapping("/thong-ke/doanh-thu-thang")
    public ResponseEntity<BigDecimal> getDoanhThuTheoThang(
            @RequestParam int month,
            @RequestParam int year
    ) {
        return ResponseEntity.ok(
                repository.getDoanhThuTheoThang(month, year)
        );
    }

    @GetMapping("/thong-ke/doanh-thu-nam")
    public ResponseEntity<BigDecimal> getDoanhThuTheoNam(
            @RequestParam int year
    ) {
        return ResponseEntity.ok(
                repository.getDoanhThuTheoNam(year)
        );
    }

    @GetMapping("/thong-ke/doanh-thu-ngay")
    public ResponseEntity<BigDecimal> getDoanhThuTheoNgay(
            @RequestParam
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            Date ngay
    ) {
        return ResponseEntity.ok(
                repository.getDoanhThuTheoNgay(ngay)
        );
    }

    @GetMapping("/thong-ke/tong-so-don")
    public ResponseEntity<Long> getTongSoDon() {
        return ResponseEntity.ok(
                repository.getTongSoDon()
        );
    }

    @GetMapping("/thong-ke/so-don-theo-trang-thai")
    public ResponseEntity<Long> getSoDonTheoTrangThai(
            @RequestParam String trangThai
    ) {
        return ResponseEntity.ok(
                repository.getSoDonTheoTrangThai(trangThai)
        );
    }

    @GetMapping("/thong-ke/dashboard")
    public ResponseEntity<ThongKeDashboardResponse> getDashboard() {
        ThongKeDashboardResponse response = new ThongKeDashboardResponse();

        response.setTongSoDon(
                repository.getTongSoDon()
        );

        response.setTongDoanhThu(
                repository.getTongDoanhThu()
        );

        response.setSoDonChoXacNhan(
                repository.getSoDonTheoTrangThai("CHO_XAC_NHAN")
        );

        response.setSoDonDaXacNhan(
                repository.getSoDonTheoTrangThai("DA_XAC_NHAN")
        );

        response.setSoDonDangGiao(
                repository.getSoDonTheoTrangThai("DANG_GIAO")
        );

        response.setSoDonHoanThanh(
                repository.getSoDonTheoTrangThai("HOAN_THANH")
        );

        response.setSoDonDaHuy(
                repository.getSoDonTheoTrangThai("DA_HUY")
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/thong-ke/doanh-thu-khoang-thoi-gian")
    public ResponseEntity<BigDecimal> getDoanhThuTheoKhoangThoiGian(
            @RequestParam
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            Date tuNgay,

            @RequestParam
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            Date denNgay
    ) {
        return ResponseEntity.ok(
                repository.getDoanhThuTheoKhoangThoiGian(
                        tuNgay,
                        denNgay
                )
        );
    }

    @GetMapping("/thong-ke/bieu-do-doanh-thu-thang")
    public ResponseEntity<?> getBieuDoDoanhThuTheoThang(
            @RequestParam int year
    ) {
        return ResponseEntity.ok(
                hoaDonService.getDoanhThuTheoThang(year)
        );
    }

    @GetMapping("/thong-ke/trang-thai-don-hang")
    public ResponseEntity<?> getThongKeTrangThaiDonHang(
            @RequestParam int year
    ) {
        return ResponseEntity.ok(
                hoaDonService.getThongKeTrangThaiDonHang(year)
        );
    }

    @GetMapping("/thong-ke/top-5-san-pham-ban-chay")
    public ResponseEntity<?> getTop5SanPhamBanChay(
            @RequestParam int year
    ) {
        return ResponseEntity.ok(
                hoaDonService.getTop5SanPhamBanChay(year)
        );
    }

    @PostMapping("/dat-hang")
    public ResponseEntity<HoaDon> datHang(
            @RequestBody DatHangRequestDTO request
    ) {
        HoaDon hoaDon = hoaDonService.datHang(request);

        return ResponseEntity.ok(hoaDon);
    }

    @GetMapping("/khach-hang/{id}")
    public ResponseEntity<List<OrderResponseDTO>> getByKhachHang(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                hoaDonService.getOrdersByCustomer(id)
        );
    }

    @PostMapping("/ap-dung-ma-giam-gia")
    public ResponseEntity<?> apDungMaGiamGia(
            @RequestBody ApDungMaGiamGiaRequest request
    ) {
        try {
            if (request.getMaPhieuGiamGia() == null
                    || request.getMaPhieuGiamGia().trim().isEmpty()) {
                throw new RuntimeException("Vui lòng nhập mã giảm giá");
            }

            if (request.getTongTienHang() == null
                    || request.getTongTienHang().compareTo(BigDecimal.ZERO) <= 0) {
                throw new RuntimeException("Tổng tiền hàng không hợp lệ");
            }

            PhieuGiamGia phieu = phieuGiamGiaRepository
                    .findByMaPhieuIgnoreCase(request.getMaPhieuGiamGia().trim())
                    .orElseThrow(() -> new RuntimeException("Mã giảm giá không tồn tại"));

            if (phieu.getTrangThai() == null || !phieu.getTrangThai()) {
                throw new RuntimeException("Mã giảm giá không hoạt động");
            }

            Date now = new Date();

            if (phieu.getNgayBatDau() != null && now.before(phieu.getNgayBatDau())) {
                throw new RuntimeException("Mã giảm giá chưa đến thời gian sử dụng");
            }

            if (phieu.getNgayKetThuc() != null && now.after(phieu.getNgayKetThuc())) {
                throw new RuntimeException("Mã giảm giá đã hết hạn");
            }

            if (phieu.getSoLuong() != null && phieu.getSoLuong() <= 0) {
                throw new RuntimeException("Mã giảm giá đã hết lượt sử dụng");
            }

            if (phieu.getGiaTriGiam() == null) {
                throw new RuntimeException("Giá trị giảm không hợp lệ");
            }

            BigDecimal tongTienHang = request.getTongTienHang();

            BigDecimal dieuKien = phieu.getGiaTriDonHangToiThieu() == null
                    ? BigDecimal.ZERO
                    : phieu.getGiaTriDonHangToiThieu();

            if (tongTienHang.compareTo(dieuKien) < 0) {
                throw new RuntimeException(
                        "Đơn hàng chưa đạt giá trị tối thiểu để dùng mã này"
                );
            }

            BigDecimal soTienGiam;

            if (Boolean.TRUE.equals(phieu.getLoaiGiamGia())) {
                soTienGiam = tongTienHang
                        .multiply(phieu.getGiaTriGiam())
                        .divide(BigDecimal.valueOf(100));
            } else {
                soTienGiam = phieu.getGiaTriGiam();
            }

            if (phieu.getGiaTriGiamToiDa() != null
                    && soTienGiam.compareTo(phieu.getGiaTriGiamToiDa()) > 0) {
                soTienGiam = phieu.getGiaTriGiamToiDa();
            }

            if (soTienGiam.compareTo(tongTienHang) > 0) {
                soTienGiam = tongTienHang;
            }

            if (soTienGiam.compareTo(BigDecimal.ZERO) < 0) {
                soTienGiam = BigDecimal.ZERO;
            }

            BigDecimal tongTienSauGiam = tongTienHang.subtract(soTienGiam);

            return ResponseEntity.ok(
                    new ApDungMaGiamGiaResponse(
                            phieu.getId().longValue(),
                            phieu.getMaPhieu(),
                            phieu.getTenPhieu(),
                            soTienGiam,
                            tongTienSauGiam,
                            "Áp dụng mã giảm giá thành công"
                    )
            );

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}