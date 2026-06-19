package com.gxsneaker.gxsneaker.service;

import com.gxsneaker.gxsneaker.dto.*;
import com.gxsneaker.gxsneaker.entity.KhachHang;
import com.gxsneaker.gxsneaker.entity.NhanVien;
import com.gxsneaker.gxsneaker.entity.OtpXacThuc;
import com.gxsneaker.gxsneaker.repository.KhachHangRepository;
import com.gxsneaker.gxsneaker.repository.NhanVienRepository;
import com.gxsneaker.gxsneaker.repository.OtpXacThucRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final KhachHangRepository khachHangRepository;
    private final NhanVienRepository nhanVienRepository;
    private final OtpXacThucRepository otpXacThucRepository;
    private final EmailService emailService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public LoginResponse login(LoginRequest request) {

        Optional<KhachHang> kh =
                khachHangRepository.findByEmail(request.getUsername());

        if (kh.isEmpty()) {
            kh = khachHangRepository.findBySoDienThoai(request.getUsername());
        }

        if (kh.isEmpty()) {
            throw new RuntimeException("Tài khoản không tồn tại");
        }

        if (Boolean.FALSE.equals(kh.get().getTrangThai())) {
            throw new RuntimeException("Tài khoản đã bị khóa");
        }

        if (!passwordEncoder.matches(request.getPassword(), kh.get().getMatKhau())) {
            throw new RuntimeException("Sai mật khẩu");
        }

        String token = jwtService.generateToken(
                kh.get().getEmail(),
                "CUSTOMER"
        );

        return new LoginResponse(
                kh.get().getId(),
                kh.get().getMaKhachHang(),
                kh.get().getHoTen(),
                kh.get().getEmail(),
                kh.get().getSoDienThoai(),
                "Đăng nhập thành công",
                "CUSTOMER",
                token
        );
    }

    public LoginResponse loginNhanVien(LoginRequest request) {

        Optional<NhanVien> nv =
                nhanVienRepository.findByEmail(request.getUsername());

        if (nv.isEmpty()) {
            nv = nhanVienRepository.findBySoDienThoai(request.getUsername());
        }

        if (nv.isEmpty()) {
            throw new RuntimeException("Tài khoản nhân viên không tồn tại");
        }

        if (Boolean.FALSE.equals(nv.get().getTrangThai())) {
            throw new RuntimeException("Tài khoản nhân viên đã bị khóa");
        }

        if (!passwordEncoder.matches(request.getPassword(), nv.get().getMatKhau())) {
            throw new RuntimeException("Sai mật khẩu");
        }

        String role = nv.get().getPhanQuyen().getMaQuyen();

        String token = jwtService.generateToken(
                nv.get().getEmail(),
                role
        );

        return new LoginResponse(
                nv.get().getId(),
                nv.get().getMaNhanVien(),
                nv.get().getHoTen(),
                nv.get().getEmail(),
                nv.get().getSoDienThoai(),
                "Đăng nhập nhân viên thành công",
                role,
                token
        );
    }

    public RegisterResponse register(RegisterRequest request) {

        if (request.getHoTen() == null || request.getHoTen().isBlank()) {
            throw new RuntimeException("Họ tên không được để trống");
        }

        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new RuntimeException("Email không được để trống");
        }

        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new RuntimeException("Mật khẩu không được để trống");
        }

        if (khachHangRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email đã tồn tại");
        }

        if (khachHangRepository.findBySoDienThoai(request.getSoDienThoai()).isPresent()) {
            throw new RuntimeException("Số điện thoại đã tồn tại");
        }

        long count = khachHangRepository.count() + 1;
        String maKhachHang = String.format("KH%03d", count);

        KhachHang khachHang = KhachHang.builder()
                .maKhachHang(maKhachHang)
                .hoTen(request.getHoTen())
                .email(request.getEmail())
                .soDienThoai(request.getSoDienThoai())
                .matKhau(passwordEncoder.encode(request.getPassword()))
                .trangThai(true)
                .daXacThuc(true)
                .build();

        khachHangRepository.save(khachHang);

        return new RegisterResponse(
                khachHang.getMaKhachHang(),
                khachHang.getHoTen(),
                khachHang.getEmail(),
                khachHang.getSoDienThoai(),
                "Đăng ký thành công"
        );
    }

    public LoginResponse me(String token) {

        String email = jwtService.extractUsername(token);

        KhachHang kh = khachHangRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản"));

        return new LoginResponse(
                kh.getId(),
                kh.getMaKhachHang(),
                kh.getHoTen(),
                kh.getEmail(),
                kh.getSoDienThoai(),
                "Lấy thông tin thành công",
                "CUSTOMER",
                token
        );
    }

    public String changePassword(String token, ChangePasswordRequest request) {

        String email = jwtService.extractUsername(token);

        KhachHang kh = khachHangRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản"));

        if (!passwordEncoder.matches(request.getOldPassword(), kh.getMatKhau())) {
            throw new RuntimeException("Mật khẩu cũ không đúng");
        }

        if (request.getNewPassword() == null || request.getNewPassword().isBlank()) {
            throw new RuntimeException("Mật khẩu mới không được để trống");
        }

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException("Xác nhận mật khẩu không khớp");
        }

        kh.setMatKhau(passwordEncoder.encode(request.getNewPassword()));
        khachHangRepository.save(kh);

        return "Đổi mật khẩu thành công";
    }

    public String forgotPassword(ForgotPasswordRequest request) {

        KhachHang kh = khachHangRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Email không tồn tại"));

        String otp = String.valueOf(100000 + new Random().nextInt(900000));

        OtpXacThuc otpXacThuc = OtpXacThuc.builder()
                .email(kh.getEmail())
                .maOtp(otp)
                .loaiOtp("FORGOT_PASSWORD")
                .thoiGianHetHan(LocalDateTime.now().plusMinutes(5))
                .daSuDung(false)
                .ngayTao(LocalDateTime.now())
                .build();

        otpXacThucRepository.save(otpXacThuc);
        emailService.sendOtpEmail(kh.getEmail(), otp);

        return "Mã OTP đã được gửi về email";
    }

    public String resetPassword(ResetPasswordRequest request) {

        KhachHang kh = khachHangRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Email không tồn tại"));

        OtpXacThuc otp = otpXacThucRepository
                .findTopByEmailAndLoaiOtpAndDaSuDungOrderByIdDesc(
                        request.getEmail(),
                        "FORGOT_PASSWORD",
                        false
                )
                .orElseThrow(() -> new RuntimeException("OTP không tồn tại"));

        if (!otp.getMaOtp().equals(request.getOtp())) {
            throw new RuntimeException("OTP không đúng");
        }

        if (otp.getThoiGianHetHan().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP đã hết hạn");
        }

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException("Xác nhận mật khẩu không khớp");
        }

        kh.setMatKhau(passwordEncoder.encode(request.getNewPassword()));
        khachHangRepository.save(kh);

        otp.setDaSuDung(true);
        otpXacThucRepository.save(otp);

        return "Đặt lại mật khẩu thành công";
    }

    public AdminLoginResponse adminLogin(AdminLoginRequest request) {

        Optional<NhanVien> nv =
                nhanVienRepository.findByEmail(request.getUsername());

        if (nv.isEmpty()) {
            nv = nhanVienRepository.findBySoDienThoai(
                    request.getUsername()
            );
        }

        if (nv.isEmpty()) {
            throw new RuntimeException("Tài khoản không tồn tại");
        }

        if (!passwordEncoder.matches(
                request.getPassword(),
                nv.get().getMatKhau()
        )) {

            throw new RuntimeException("Sai mật khẩu");
        }

        String role =
                nv.get().getPhanQuyen().getMaQuyen();

        String token =
                jwtService.generateToken(
                        nv.get().getEmail(),
                        role
                );

        return new AdminLoginResponse(
                nv.get().getId(),
                nv.get().getMaNhanVien(),
                nv.get().getHoTen(),
                nv.get().getEmail(),
                role,
                token,
                "Đăng nhập thành công"
        );
    }
}