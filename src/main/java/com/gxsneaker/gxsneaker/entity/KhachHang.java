package com.gxsneaker.gxsneaker.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Date;

@Entity
@Table(name = "khach_hang")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KhachHang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "ma_khach_hang")
    private String maKhachHang;

    @Column(name = "ho_ten", columnDefinition = "NVARCHAR(255)")
    private String hoTen;

    private String email;

    @Column(name = "mat_khau")
    private String matKhau;

    @Column(name = "so_dien_thoai")
    private String soDienThoai;

    @Column(name = "gioi_tinh")
    private Boolean gioiTinh;

    @Column(name = "ngay_sinh")
    private LocalDate ngaySinh;

    private String avatar;

    @Column(name = "trang_thai")
    private Boolean trangThai;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ngay_tao")
    private Date ngayTao;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ngay_cap_nhat")
    private Date ngayCapNhat;

    @Column(name = "da_xac_thuc")
    private Boolean daXacThuc;
    @OneToMany(mappedBy = "khachHang")
    @JsonIgnore
    private List<HoaDon> hoaDons;

    @PrePersist
    public void prePersist() {
        ngayTao = new Date();

        if (trangThai == null) {
            trangThai = true;
        }

        if (daXacThuc == null) {
            daXacThuc = false;
        }
    }

    @PreUpdate
    public void preUpdate() {
        ngayCapNhat = new Date();
    }
}