package com.gxsneaker.gxsneaker.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "HOA_DON")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HoaDon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_khach_hang")
    private Long idKhachHang;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "id_khach_hang",
            insertable = false,
            updatable = false
    )
    @JsonIgnore
    private KhachHang khachHang;

    private Long idNhanVien;

    private Long idPhieuGiamGia;

    private Long idDonViVanChuyen;

    private String maHoaDon;

    private String maVanDon;

    private String loaiDon;

    private BigDecimal tongTienHang;

    private BigDecimal soTienGiam;

    private BigDecimal phiVanChuyen;

    private BigDecimal tongTienThanhToan;

    private String tenNguoiNhan;

    private String soDienThoaiNguoiNhan;
    @Column(name = "dia_chi_nguoi_nhan")
    private String diaChiNguoiNhan;

    private String emailNguoiNhan;

    private String trangThai;

    private String ghiChu;

    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayDatHang;

    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayXacNhan;

    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayGiaoHang;

    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayHoanThanh;

    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayHuy;

    private String lyDoHuy;

    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayTao;

    private String nguoiTao;

    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayCapNhat;

    private String nguoiCapNhat;



    @OneToMany(mappedBy = "hoaDon")
    @JsonManagedReference
    private List<HoaDonChiTiet> hoaDonChiTiets;
}