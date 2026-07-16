package com.gxsneaker.gxsneaker.repository;

import com.gxsneaker.gxsneaker.dto.TopSanPhamBanChayDTO;
import com.gxsneaker.gxsneaker.entity.HoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.awt.print.Pageable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Date;


public interface HoaDonRepository extends JpaRepository<HoaDon, Long> {
    List<HoaDon> findByMaHoaDonContaining(String maHoaDon);

    List<HoaDon> findByTrangThai(String trangThai);
    List<HoaDon> findByIdKhachHang(Long idKhachHang);
    List<HoaDon> findByMaHoaDonContainingAndTrangThai(
            String maHoaDon,
            String trangThai
    );

    // ==========================================
    // CHỨC NĂNG: LỌC HÓA ĐƠN THEO NGÀY ĐẶT HÀNG
    // NGHIỆP VỤ:
    // Lấy danh sách hóa đơn trong khoảng thời gian
    // ==========================================
    List<HoaDon> findByNgayDatHangBetween(
            Date from,
            Date to
    );
    // =========================================
// THỐNG KÊ TỔNG DOANH THU
// =========================================
    @Query(value = """
SELECT ISNULL(SUM(tong_tien_thanh_toan),0)
FROM HOA_DON
WHERE trang_thai = 'HOAN_THANH'
""", nativeQuery = true)
    BigDecimal getTongDoanhThu();

    // =========================================
// THỐNG KÊ DOANH THU THEO NGÀY
// =========================================
    @Query(value = """
SELECT ISNULL(SUM(tong_tien_thanh_toan),0)
FROM HOA_DON
WHERE trang_thai = 'HOAN_THANH'
AND CONVERT(date, ngay_hoan_thanh) = :ngay
""", nativeQuery = true)
    BigDecimal getDoanhThuTheoNgay(
            @Param("ngay") Date ngay
    );

    // =========================================
// THỐNG KÊ DOANH THU THEO THÁNG
// =========================================
    @Query(value = """
SELECT ISNULL(SUM(tong_tien_thanh_toan),0)
FROM HOA_DON
WHERE trang_thai = 'HOAN_THANH'
AND MONTH(ngay_hoan_thanh) = :month
AND YEAR(ngay_hoan_thanh) = :year
""", nativeQuery = true)
    BigDecimal getDoanhThuTheoThang(
            @Param("month") int month,
            @Param("year") int year
    );

    // =========================================
// THỐNG KÊ DOANH THU THEO NĂM
// =========================================
    @Query(value = """
SELECT ISNULL(SUM(tong_tien_thanh_toan),0)
FROM HOA_DON
WHERE trang_thai = 'HOAN_THANH'
AND YEAR(ngay_hoan_thanh) = :year
""", nativeQuery = true)
    BigDecimal getDoanhThuTheoNam(
            @Param("year") int year
    );

    // =========================================
// THỐNG KÊ DOANH THU THEO KHOẢNG THỜI GIAN
// =========================================
    @Query(value = """
SELECT ISNULL(SUM(tong_tien_thanh_toan),0)
FROM HOA_DON
WHERE trang_thai = 'HOAN_THANH'
AND CONVERT(date, ngay_hoan_thanh)
BETWEEN :tuNgay AND :denNgay
""", nativeQuery = true)
    BigDecimal getDoanhThuTheoKhoangThoiGian(
            @Param("tuNgay") Date tuNgay,
            @Param("denNgay") Date denNgay
    );
    // =========================================
// THỐNG KÊ TỔNG SỐ ĐƠN HÀNG
// =========================================
    @Query("""
    SELECT COUNT(h)
    FROM HoaDon h
    """)
    Long getTongSoDon();


    // =========================================
// THỐNG KÊ SỐ ĐƠN THEO TRẠNG THÁI
// =========================================
    @Query("""
    SELECT COUNT(h)
    FROM HoaDon h
    WHERE h.trangThai = :trangThai
    """)
    Long getSoDonTheoTrangThai(
            @Param("trangThai") String trangThai
    );

    // =========================================
// BIỂU ĐỒ THỐNG KÊ THEO THÁNG
// =========================================
//    @Query(value = """
//    SELECT
//        MONTH(ngay_tao) AS thang,
//        ISNULL(SUM(tong_tien_thanh_toan),0) AS doanhThu
//    FROM HOA_DON
//    WHERE trang_thai = 'HOAN_THANH'
//      AND YEAR(ngay_tao) = YEAR(GETDATE())
//    GROUP BY MONTH(ngay_tao)
//    ORDER BY MONTH(ngay_tao)
//    """, nativeQuery = true)
//    List<Object[]> getDoanhThuTheoThang();

    @Query(value = """
WITH THANG AS (
    SELECT 1 AS thang
    UNION ALL SELECT 2
    UNION ALL SELECT 3
    UNION ALL SELECT 4
    UNION ALL SELECT 5
    UNION ALL SELECT 6
    UNION ALL SELECT 7
    UNION ALL SELECT 8
    UNION ALL SELECT 9
    UNION ALL SELECT 10
    UNION ALL SELECT 11
    UNION ALL SELECT 12
)
SELECT
    T.thang,
    ISNULL(SUM(HD.tong_tien_thanh_toan),0) AS doanhThu
FROM THANG T
LEFT JOIN HOA_DON HD
    ON MONTH(HD.ngay_hoan_thanh) = T.thang
    AND YEAR(HD.ngay_hoan_thanh) = :year
    AND HD.trang_thai = 'HOAN_THANH'
GROUP BY T.thang
ORDER BY T.thang
""", nativeQuery = true)
    List<Object[]> getDoanhThuTheoThang(@Param("year") Integer year);


    @Query(value = """
SELECT
    trang_thai,
    COUNT(*) AS soLuong
FROM HOA_DON
GROUP BY trang_thai
""", nativeQuery = true)
    List<Object[]> getThongKeTrangThaiDonHang();


    @Query(value = """
SELECT
    trang_thai,
    COUNT(*) AS soLuong
FROM HOA_DON
WHERE YEAR(ngay_dat_hang) = :year
GROUP BY trang_thai
""", nativeQuery = true)
    List<Object[]> getThongKeTrangThaiDonHang(
            @Param("year") int year
    );


    @Query(value = """
SELECT TOP 5
    sp.ten_san_pham AS tenSanPham,
    SUM(hdct.so_luong) AS tongSoLuongBan
FROM HOA_DON_CHI_TIET hdct
JOIN HOA_DON hd
    ON hd.id = hdct.id_hoa_don
JOIN CHI_TIET_SAN_PHAM ctsp
    ON ctsp.id = hdct.id_chi_tiet_san_pham
JOIN SAN_PHAM sp
    ON sp.id = ctsp.id_san_pham
WHERE hd.trang_thai = 'HOAN_THANH'
AND YEAR(hd.ngay_hoan_thanh) = :year
GROUP BY sp.ten_san_pham
ORDER BY SUM(hdct.so_luong) DESC
""", nativeQuery = true)
    List<Object[]> getTop5SanPhamBanChay(
            @Param("year") Integer year
    );


    //Đơn hàng
    @Query("SELECT COUNT(h) FROM HoaDon h")
    Long countHoaDon();
}