package com.poly.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequestMapping("/api/upload")
@CrossOrigin("*") // Cho phép Frontend gọi sang Backend
public class FileUploadController {

    // Tên thư mục lưu ảnh (nằm ngay tại thư mục gốc dự án)
    private static final String UPLOAD_DIR = "uploads/";

    @PostMapping
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File bị rỗng!");
        }

        try {
            // Lấy đường dẫn tuyệt đối của dự án dựa trên đường dẫn tương đối "uploads/"
            // Điều này giúp dự án không phụ thuộc ổ đĩa C hay D.
            Path uploadPath = Paths.get(UPLOAD_DIR).toAbsolutePath().normalize();
            File directory = uploadPath.toFile();
            if (!directory.exists()) {
                directory.mkdirs(); // Tạo thư mục nếu chưa có
            }

            // Xử lý tên file: Đổi tên thành UUID để không bao giờ bị trùng
            String originalFileName = file.getOriginalFilename();
            String fileExtension = "";
            if (originalFileName != null && originalFileName.contains(".")) {
                fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            }

            String newFileName = UUID.randomUUID().toString() + fileExtension;
            Path filePath = uploadPath.resolve(newFileName); // Nối đường dẫn tuyệt đối với tên file mới

            // Lưu file xuống ổ cứng vật lý
            Files.write(filePath, file.getBytes());

            // TRỌNG TÂM YÊU CẦU: Trả về một link URL hoàn chỉnh dạng localhost
            // Link này sẽ được Frontend điền vào Database
            String fileUrl = "http://localhost:8080/uploads/" + newFileName;
            return ResponseEntity.ok(fileUrl);

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi lưu file!");
        }
    }
}