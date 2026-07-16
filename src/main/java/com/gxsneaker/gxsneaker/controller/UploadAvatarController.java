package com.gxsneaker.gxsneaker.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@RestController
@RequestMapping("/api/upload")
@CrossOrigin("*")
public class UploadAvatarController {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @PostMapping("/avatar")
    public ResponseEntity<String> uploadAvatar(
            @RequestParam("file") MultipartFile file
    ) {

        try {

            String fileName =
                    UUID.randomUUID() + "_"
                            + StringUtils.cleanPath(file.getOriginalFilename());

            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {

                Files.createDirectories(uploadPath);

            }

            Files.copy(
                    file.getInputStream(),
                    uploadPath.resolve(fileName),
                    StandardCopyOption.REPLACE_EXISTING
            );

            return ResponseEntity.ok(fileName);

        } catch (Exception e) {

            return ResponseEntity.badRequest().body("Upload thất bại");

        }

    }

}