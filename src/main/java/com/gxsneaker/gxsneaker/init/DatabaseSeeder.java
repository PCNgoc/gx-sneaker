package com.gxsneaker.gxsneaker.init;

import com.gxsneaker.gxsneaker.entity.*;
import com.gxsneaker.gxsneaker.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    @Autowired
    private ThuongHieuRepository thuongHieuRepository;

    @Autowired
    private XuatXuRepository xuatXuRepository;

    @Autowired
    private ChatLieuRepository chatLieuRepository;

    @Autowired
    private CoGiayRepository coGiayRepository;

    @Autowired
    private DeGiayRepository deGiayRepository;

    @Autowired
    private DanhMucRepository danhMucRepository;

    @Autowired
    private MauSacRepository mauSacRepository;

    @Autowired
    private KichThuocRepository kichThuocRepository;

    @Autowired
    private SanPhamRepository sanPhamRepository;

    @Autowired
    private ChiTietSanPhamRepository chiTietSanPhamRepository;

    @Override
    public void run(String... args) throws Exception {
        if (sanPhamRepository.count() > 0) {
            System.out.println("Sản phẩm đã tồn tại trong database, bỏ qua seeding.");
            return;
        }

        System.out.println("Bắt đầu seeding dữ liệu sản phẩm mẫu...");

        // 1. Thương hiệu
        ThuongHieu nike = ThuongHieu.builder()
                .ma("TH01")
                .ten("Nike")
                .logo("nike_logo.png")
                .website("https://nike.com")
                .moTa("Thương hiệu thể thao hàng đầu thế giới")
                .quocGia("USA")
                .trangThai(true)
                .build();
        nike = thuongHieuRepository.save(nike);

        ThuongHieu adidas = ThuongHieu.builder()
                .ma("TH02")
                .ten("Adidas")
                .logo("adidas_logo.png")
                .website("https://adidas.com")
                .moTa("Thương hiệu thể thao đến từ Đức")
                .quocGia("Germany")
                .trangThai(true)
                .build();
        adidas = thuongHieuRepository.save(adidas);

        // 2. Xuất xứ
        XuatXu vn = XuatXu.builder()
                .ma("XX01")
                .ten("Việt Nam")
                .moTa("Sản xuất tại Việt Nam")
                .trangThai(true)
                .build();
        vn = xuatXuRepository.save(vn);

        XuatXu cn = XuatXu.builder()
                .ma("XX02")
                .ten("Trung Quốc")
                .moTa("Sản xuất tại Trung Quốc")
                .trangThai(true)
                .build();
        cn = xuatXuRepository.save(cn);

        // 3. Chất liệu
        ChatLieu da = ChatLieu.builder()
                .ma("CL01")
                .ten("Da tự nhiên")
                .moTa("Chất liệu da thật cao cấp")
                .trangThai(true)
                .build();
        da = chatLieuRepository.save(da);

        ChatLieu vai = ChatLieu.builder()
                .ma("CL02")
                .ten("Vải lưới mesh")
                .moTa("Vải lưới thoáng khí")
                .trangThai(true)
                .build();
        vai = chatLieuRepository.save(vai);

        // 4. Cổ giày
        CoGiay coThap = CoGiay.builder()
                .ma("CG01")
                .ten("Cổ thấp (Low)")
                .moTa("Thiết kế cổ thấp năng động")
                .trangThai(true)
                .build();
        coThap = coGiayRepository.save(coThap);

        CoGiay coCao = CoGiay.builder()
                .ma("CG02")
                .ten("Cổ cao (High)")
                .moTa("Thiết kế cổ cao cá tính")
                .trangThai(true)
                .build();
        coCao = coGiayRepository.save(coCao);

        // 5. Đế giày
        DeGiay deCaoSu = DeGiay.builder()
                .ma("DG01")
                .ten("Đế cao su chống trượt")
                .moTa("Chống trơn trượt cực tốt")
                .trangThai(true)
                .build();
        deCaoSu = deGiayRepository.save(deCaoSu);

        // 6. Danh mục
        DanhMuc dmSneaker = new DanhMuc();
        dmSneaker.setMa("DM01");
        dmSneaker.setTen("Giày Sneaker Thời Trang");
        dmSneaker.setMoTa("Các mẫu giày sneaker đi hằng ngày");
        dmSneaker.setThuTuHienThi(1);
        dmSneaker.setTrangThai(true);
        dmSneaker.setNgayTao(new Date());
        dmSneaker = danhMucRepository.save(dmSneaker);

        DanhMuc dmChayBo = new DanhMuc();
        dmChayBo.setMa("DM02");
        dmChayBo.setTen("Giày Chạy Bộ");
        dmChayBo.setMoTa("Các mẫu giày chuyên dụng cho chạy bộ");
        dmChayBo.setThuTuHienThi(2);
        dmChayBo.setTrangThai(true);
        dmChayBo.setNgayTao(new Date());
        dmChayBo = danhMucRepository.save(dmChayBo);

        // 7. Màu sắc
        MauSac trang = MauSac.builder()
                .ma("MS01")
                .ten("Màu Trắng")
                .maHex("#FFFFFF")
                .trangThai(true)
                .build();
        trang = mauSacRepository.save(trang);

        MauSac den = MauSac.builder()
                .ma("MS02")
                .ten("Màu Đen")
                .maHex("#000000")
                .trangThai(true)
                .build();
        den = mauSacRepository.save(den);

        // 8. Kích thước
        KichThuoc size40 = KichThuoc.builder()
                .ma("KT40")
                .size(40)
                .trangThai(true)
                .build();
        size40 = kichThuocRepository.save(size40);

        KichThuoc size41 = KichThuoc.builder()
                .ma("KT41")
                .size(41)
                .trangThai(true)
                .build();
        size41 = kichThuocRepository.save(size41);

        KichThuoc size42 = KichThuoc.builder()
                .ma("KT42")
                .size(42)
                .trangThai(true)
                .build();
        size42 = kichThuocRepository.save(size42);

        // 9. Sản phẩm
        SanPham af1 = SanPham.builder()
                .thuongHieu(nike)
                .xuatXu(vn)
                .chatLieu(da)
                .coGiay(coThap)
                .deGiay(deCaoSu)
                .danhMuc(dmSneaker)
                .maSanPham("SP001")
                .tenSanPham("Nike Air Force 1 '07")
                .gioiTinh("Unisex")
                .moTaNgan("Mẫu giày sneaker huyền thoại của nhà Nike phù hợp với mọi phong cách thời trang.")
                .moTaChiTiet("Giày Nike Air Force 1 '07 thiết kế nguyên bản với các đường nét tinh tế, chất liệu da thật mang lại độ bền cao cùng bộ đệm Air êm ái.")
                .anhDaiDien("https://images.unsplash.com/photo-1600185365926-3a2ce3cdb9eb")
                .trangThai(true)
                .nguoiTao("System Seeder")
                .build();
        af1 = sanPhamRepository.save(af1);

        SanPham ultraboost = SanPham.builder()
                .thuongHieu(adidas)
                .xuatXu(cn)
                .chatLieu(vai)
                .coGiay(coThap)
                .deGiay(deCaoSu)
                .danhMuc(dmChayBo)
                .maSanPham("SP002")
                .tenSanPham("Adidas Ultraboost Light")
                .gioiTinh("Nam")
                .moTaNgan("Đỉnh cao của sự êm ái với công nghệ đế Boost nhẹ hơn 30% so với trước đây.")
                .moTaChiTiet("Dòng sản phẩm chạy bộ phân khúc cao cấp của Adidas sử dụng Primeknit thân thiện môi trường kết hợp đế đệm Ultraboost êm ái tối đa.")
                .anhDaiDien("https://images.unsplash.com/photo-1587563871167-1ee9c731aefb")
                .trangThai(true)
                .nguoiTao("System Seeder")
                .build();
        ultraboost = sanPhamRepository.save(ultraboost);

        // 10. Chi tiết sản phẩm
        // AF1 trắng size 40, 41
        ChiTietSanPham ct1 = ChiTietSanPham.builder()
                .sanPham(af1)
                .mauSac(trang)
                .kichThuoc(size40)
                .maChiTiet("SP001-MS01-KT40")
                .soLuongTon(50)
                .giaNhap(new BigDecimal("1200000"))
                .giaBan(new BigDecimal("2500000"))
                .trangThai(true)
                .build();
        chiTietSanPhamRepository.save(ct1);

        ChiTietSanPham ct2 = ChiTietSanPham.builder()
                .sanPham(af1)
                .mauSac(trang)
                .kichThuoc(size41)
                .maChiTiet("SP001-MS01-KT41")
                .soLuongTon(50)
                .giaNhap(new BigDecimal("1200000"))
                .giaBan(new BigDecimal("2500000"))
                .trangThai(true)
                .build();
        chiTietSanPhamRepository.save(ct2);

        // AF1 đen size 41
        ChiTietSanPham ct3 = ChiTietSanPham.builder()
                .sanPham(af1)
                .mauSac(den)
                .kichThuoc(size41)
                .maChiTiet("SP001-MS02-KT41")
                .soLuongTon(30)
                .giaNhap(new BigDecimal("1250000"))
                .giaBan(new BigDecimal("2600000"))
                .trangThai(true)
                .build();
        chiTietSanPhamRepository.save(ct3);

        // Ultraboost đen size 41, 42
        ChiTietSanPham ct4 = ChiTietSanPham.builder()
                .sanPham(ultraboost)
                .mauSac(den)
                .kichThuoc(size41)
                .maChiTiet("SP002-MS02-KT41")
                .soLuongTon(40)
                .giaNhap(new BigDecimal("1900000"))
                .giaBan(new BigDecimal("3800000"))
                .trangThai(true)
                .build();
        chiTietSanPhamRepository.save(ct4);

        ChiTietSanPham ct5 = ChiTietSanPham.builder()
                .sanPham(ultraboost)
                .mauSac(den)
                .kichThuoc(size42)
                .maChiTiet("SP002-MS02-KT42")
                .soLuongTon(45)
                .giaNhap(new BigDecimal("1900000"))
                .giaBan(new BigDecimal("3800000"))
                .trangThai(true)
                .build();
        chiTietSanPhamRepository.save(ct5);

        System.out.println("Seeding dữ liệu thành công!");
    }
}
