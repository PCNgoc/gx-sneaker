package com.gxsneaker.gxsneaker.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ChiTietSanPhamDTO {

    private Long id;
    private Long idSanPham;
    private Long idMauSac;
    private Long idKichThuoc;
    private String maChiTiet;
    private Integer soLuongTon;
    private BigDecimal giaNhap;
    private BigDecimal giaBan;
    private Boolean trangThai;

    // hiển thị
    private String tenSanPham;
    private String tenMauSac;
    private Integer size;
    private String hinhAnh;

    // Helper classes to support nested structures expected by frontend
    public static class SanPhamNested {
        private final String ten;
        public SanPhamNested(String ten) {
            this.ten = ten;
        }
        public String getTen() {
            return ten;
        }
    }

    public static class MauSacNested {
        private final String ten;
        public MauSacNested(String ten) {
            this.ten = ten;
        }
        public String getTen() {
            return ten;
        }
    }

    public static class KichThuocNested {
        private final Integer size;
        public KichThuocNested(Integer size) {
            this.size = size;
        }
        public Integer getSize() {
            return size;
        }
        public String getTen() {
            return String.valueOf(size);
        }
    }

    public SanPhamNested getSanPham() {
        return tenSanPham != null ? new SanPhamNested(tenSanPham) : null;
    }

    public MauSacNested getMauSac() {
        return tenMauSac != null ? new MauSacNested(tenMauSac) : null;
    }

    public KichThuocNested getKichThuoc() {
        return size != null ? new KichThuocNested(size) : null;
    }
}