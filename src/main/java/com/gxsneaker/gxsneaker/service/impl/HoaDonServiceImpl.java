package com.gxsneaker.gxsneaker.service.impl;

import com.gxsneaker.gxsneaker.dto.*;
import com.gxsneaker.gxsneaker.entity.ChiTietSanPham;
import com.gxsneaker.gxsneaker.entity.HoaDon;
import com.gxsneaker.gxsneaker.entity.HoaDonChiTiet;
import com.gxsneaker.gxsneaker.entity.PhieuGiamGia;
import com.gxsneaker.gxsneaker.repository.ChiTietSanPhamRepository;
import com.gxsneaker.gxsneaker.repository.HoaDonChiTietRepository;
import com.gxsneaker.gxsneaker.repository.HoaDonRepository;
import com.gxsneaker.gxsneaker.repository.PhieuGiamGiaRepository;
import com.gxsneaker.gxsneaker.service.HoaDonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HoaDonServiceImpl implements HoaDonService {

    private final HoaDonRepository hoaDonRepository;
    private final HoaDonChiTietRepository hoaDonChiTietRepository;
    private final ChiTietSanPhamRepository chiTietSanPhamRepository;
    private final PhieuGiamGiaRepository phieuGiamGiaRepository;

    @Override
    public List<DoanhThuTheoThangDTO> getDoanhThuTheoThang(int year) {
        List<Object[]> data = hoaDonRepository.getDoanhThuTheoThang(year);

        return data.stream()
                .map(item -> new DoanhThuTheoThangDTO(
                        ((Number) item[0]).intValue(),
                        (BigDecimal) item[1]
                ))
                .toList();
    }

    @Override
    public List<TrangThaiDonHangDTO> getThongKeTrangThaiDonHang(int year) {
        List<Object[]> data = hoaDonRepository.getThongKeTrangThaiDonHang(year);

        return data.stream()
                .map(item -> new TrangThaiDonHangDTO(
                        (String) item[0],
                        ((Number) item[1]).longValue()
                ))
                .toList();
    }

    @Override
    public List<TopSanPhamBanChayDTO> getTop5SanPhamBanChay(int year) {
        List<Object[]> data = hoaDonRepository.getTop5SanPhamBanChay(year);

        return data.stream()
                .map(item -> new TopSanPhamBanChayDTO(
                        (String) item[0],
                        ((Number) item[1]).longValue()
                ))
                .toList();
    }

    @Override
    @Transactional
    public HoaDon datHang(DatHangRequestDTO request) {

        HoaDon hoaDon = new HoaDon();

        hoaDon.setIdKhachHang(request.getIdKhachHang());
        hoaDon.setLoaiDon("ONLINE");
        hoaDon.setTrangThai("CHO_XAC_NHAN");
        hoaDon.setNgayDatHang(new Date());
        hoaDon.setNgayTao(new Date());
        hoaDon.setNguoiTao("CUSTOMER");
        hoaDon.setTenNguoiNhan(request.getTenNguoiNhan());
        hoaDon.setSoDienThoaiNguoiNhan(request.getSoDienThoai());
        hoaDon.setDiaChiNguoiNhan(request.getDiaChi());
        hoaDon.setGhiChu(request.getGhiChu());

        Long count = hoaDonRepository.countHoaDon() + 1;
        hoaDon.setMaHoaDon(String.format("HD%06d", count));

        BigDecimal tongTienHang = BigDecimal.ZERO;

        hoaDon = hoaDonRepository.save(hoaDon);

        for (DatHangItemDTO item : request.getItems()) {

            ChiTietSanPham ctsp = chiTietSanPhamRepository
                    .findById(item.getChiTietSanPhamId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));

            if (ctsp.getSoLuongTon() < item.getSoLuong()) {
                throw new RuntimeException(
                        ctsp.getSanPham().getTenSanPham() + " không đủ tồn kho"
                );
            }

            BigDecimal thanhTien = ctsp.getGiaBan()
                    .multiply(BigDecimal.valueOf(item.getSoLuong()));

            HoaDonChiTiet hdct = HoaDonChiTiet.builder()
                    .hoaDon(hoaDon)
                    .chiTietSanPham(ctsp)
                    .soLuong(item.getSoLuong())
                    .donGia(ctsp.getGiaBan())
                    .thanhTien(thanhTien)
                    .build();

            hoaDonChiTietRepository.save(hdct);

            ctsp.setSoLuongTon(ctsp.getSoLuongTon() - item.getSoLuong());
            chiTietSanPhamRepository.save(ctsp);

            tongTienHang = tongTienHang.add(thanhTien);
        }

        BigDecimal phiVanChuyen = BigDecimal.valueOf(30000);
        BigDecimal soTienGiam = BigDecimal.ZERO;

        hoaDon.setTongTienHang(tongTienHang);

        if (request.getMaPhieuGiamGia() != null
                && !request.getMaPhieuGiamGia().trim().isEmpty()) {

            PhieuGiamGia phieu = phieuGiamGiaRepository
                    .findByMaPhieuIgnoreCase(request.getMaPhieuGiamGia().trim())
                    .orElseThrow(() -> new RuntimeException("Mã giảm giá không tồn tại"));

            soTienGiam = tinhTienGiam(phieu, tongTienHang);

            hoaDon.setIdPhieuGiamGia(phieu.getId().longValue());

            if (phieu.getSoLuong() != null && phieu.getSoLuong() > 0) {
                phieu.setSoLuong(phieu.getSoLuong() - 1);
                phieuGiamGiaRepository.save(phieu);
            }
        }

        hoaDon.setSoTienGiam(soTienGiam);
        hoaDon.setPhiVanChuyen(phiVanChuyen);
        hoaDon.setTongTienThanhToan(
                tongTienHang.subtract(soTienGiam).add(phiVanChuyen)
        );

        return hoaDonRepository.save(hoaDon);
    }

    private BigDecimal tinhTienGiam(PhieuGiamGia phieu, BigDecimal tongTienHang) {

        if (phieu == null) {
            return BigDecimal.ZERO;
        }

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

        BigDecimal dieuKienToiThieu = phieu.getGiaTriDonHangToiThieu() == null
                ? BigDecimal.ZERO
                : phieu.getGiaTriDonHangToiThieu();

        if (tongTienHang.compareTo(dieuKienToiThieu) < 0) {
            throw new RuntimeException("Đơn hàng chưa đạt giá trị tối thiểu để dùng mã này");
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

        return soTienGiam;
    }

    @Override
    public List<OrderResponseDTO> getOrdersByCustomer(Long customerId) {

        List<HoaDon> hoaDons = hoaDonRepository.findByIdKhachHang(customerId);

        return hoaDons.stream().map(hoaDon -> {

            List<OrderItemResponseDTO> items =
                    hoaDon.getHoaDonChiTiets()
                            .stream()
                            .map(ct -> OrderItemResponseDTO.builder()
                                    .chiTietSanPhamId(ct.getChiTietSanPham().getId())
                                    .productName(ct.getChiTietSanPham().getSanPham().getTenSanPham())
                                    .image(ct.getChiTietSanPham().getSanPham().getAnhDaiDien())
                                    .color(ct.getChiTietSanPham().getMauSac().getTen())
                                    .size(ct.getChiTietSanPham().getKichThuoc().getSize())
                                    .quantity(ct.getSoLuong())
                                    .price(ct.getDonGia())
                                    .total(ct.getThanhTien())
                                    .build())
                            .toList();

            return OrderResponseDTO.builder()
                    .id(hoaDon.getId())
                    .maHoaDon(hoaDon.getMaHoaDon())
                    .ngayDatHang(hoaDon.getNgayDatHang())
                    .trangThai(hoaDon.getTrangThai())
                    .tenNguoiNhan(hoaDon.getTenNguoiNhan())
                    .soDienThoai(hoaDon.getSoDienThoaiNguoiNhan())
                    .diaChi(hoaDon.getDiaChiNguoiNhan())
                    .tongTien(hoaDon.getTongTienThanhToan())
                    .items(items)
                    .build();

        }).toList();
    }

    @Override
    public OrderResponseDTO getOrderById(Long id) {

        HoaDon hoaDon = hoaDonRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng"));

        return convertToDTO(hoaDon);
    }

    private OrderResponseDTO convertToDTO(HoaDon hd) {

        List<OrderItemResponseDTO> items =
                hd.getHoaDonChiTiets()
                        .stream()
                        .map(ct -> OrderItemResponseDTO.builder()
                                .chiTietSanPhamId(ct.getChiTietSanPham().getId())
                                .productName(ct.getChiTietSanPham().getSanPham().getTenSanPham())
                                .image(ct.getChiTietSanPham().getSanPham().getAnhDaiDien())
                                .color(ct.getChiTietSanPham().getMauSac().getTen())
                                .size(ct.getChiTietSanPham().getKichThuoc().getSize())
                                .quantity(ct.getSoLuong())
                                .price(ct.getDonGia())
                                .total(ct.getThanhTien())
                                .build())
                        .toList();

        return OrderResponseDTO.builder()
                .id(hd.getId())
                .maHoaDon(hd.getMaHoaDon())
                .ngayDatHang(hd.getNgayDatHang())
                .trangThai(hd.getTrangThai())
                .tenNguoiNhan(hd.getTenNguoiNhan())
                .soDienThoai(hd.getSoDienThoaiNguoiNhan())
                .diaChi(hd.getDiaChiNguoiNhan())
                .tongTien(hd.getTongTienThanhToan())
                .items(items)
                .build();
    }
}