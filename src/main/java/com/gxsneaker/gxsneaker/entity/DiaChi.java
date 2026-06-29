package com.gxsneaker.gxsneaker.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "dia_chi")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiaChi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_khach_hang")
    private KhachHang khachHang;

    @Column(name = "ho_ten_nguoi_nhan", columnDefinition = "NVARCHAR(255)")
    private String hoTenNguoiNhan;

    @Column(name = "so_dien_thoai")
    private String soDienThoai;

    @Column(name = "tinh_thanh", columnDefinition = "NVARCHAR(255)")
    private String tinhThanh;

    @Column(name = "quan_huyen", columnDefinition = "NVARCHAR(255)")
    private String quanHuyen;

    @Column(name = "phuong_xa", columnDefinition = "NVARCHAR(255)")
    private String phuongXa;

    @Column(name = "dia_chi_chi_tiet", columnDefinition = "NVARCHAR(255)")
    private String diaChiChiTiet;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ngay_tao")
    private Date ngayTao;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ngay_cap_nhat")
    private Date ngayCapNhat;

    @PrePersist
    public void prePersist() {
        ngayTao = new Date();
    }

    @PreUpdate
    public void preUpdate() {
        ngayCapNhat = new Date();
    }
}
