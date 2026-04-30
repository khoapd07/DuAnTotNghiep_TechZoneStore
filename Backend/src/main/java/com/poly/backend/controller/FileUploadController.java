package com.poly.backend.controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/upload")
@CrossOrigin("*")
@RequiredArgsConstructor // Tự động inject Cloudinary bean
public class FileUploadController {

    private final Cloudinary cloudinary;

    @PostMapping
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File bị rỗng!");
        }

        try {
            // 1. Upload file trực tiếp lên Cloudinary
            // ObjectUtils.asMap("resource_type", "auto") giúp tự động nhận diện ảnh/video/raw
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                    ObjectUtils.asMap("resource_type", "auto"));

            // 2. Lấy link HTTPS (secure_url) do Cloudinary trả về
            // Link này sẽ có dạng: https://res.cloudinary.com/dtlmudbzh/image/upload/...
            String fileUrl = uploadResult.get("secure_url").toString();

            // 3. Trả về link xịn cho Frontend
            return ResponseEntity.ok(fileUrl);

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi upload lên Cloudinary!");
        }
    }
}