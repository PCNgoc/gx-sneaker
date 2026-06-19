package com.gxsneaker.gxsneaker.controller;

import com.gxsneaker.gxsneaker.dto.*;
import com.gxsneaker.gxsneaker.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            return ResponseEntity.ok(authService.login(request));
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {

        try {
            return ResponseEntity.ok(authService.register(request));
        } catch (RuntimeException e) {

            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());

            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");
            return ResponseEntity.ok(authService.me(token));
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody ChangePasswordRequest request) {

        try {
            String token = authHeader.replace("Bearer ", "");

            Map<String, String> result = new HashMap<>();
            result.put("message", authService.changePassword(token, request));

            return ResponseEntity.ok(result);

        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        try {
            Map<String, String> result = new HashMap<>();
            result.put("message", authService.forgotPassword(request));
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        try {
            Map<String, String> result = new HashMap<>();
            result.put("message", authService.resetPassword(request));
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping("/admin/login")
    public ResponseEntity<?> adminLogin(
            @RequestBody AdminLoginRequest request
    ) {

        return ResponseEntity.ok(
                authService.adminLogin(request)
        );
    }
}