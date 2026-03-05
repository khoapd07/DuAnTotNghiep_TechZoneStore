package com.poly.backend.security;

import com.poly.backend.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    // Khóa bí mật để ký Token (Phải đủ dài, ít nhất 32 ký tự)
    // Thực tế sẽ cấu hình trong application.properties, nhưng nay code cứng cho nhanh
    private final String JWT_SECRET = "DayLaKhoaBiMatCuaDuAnTotNghiepTechZoneStore2026QuaDaiVaBaoMat";

    // Thời gian sống của Token: 24 giờ (tính bằng milliseconds)
    private final long JWT_EXPIRATION = 86400000L;

    // Hàm tạo mã hóa khóa
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(JWT_SECRET.getBytes());
    }

    // Hàm sinh JWT Token từ thông tin User
    public String generateToken(User user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION);

        return Jwts.builder()
                .setSubject(user.getUsername()) // Subject thường lưu username
                .claim("userId", user.getUserId()) // Thêm dữ liệu tùy chọn (claims)
                .claim("role", user.getRole().getRoleName())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // 1. Lấy thông tin (Claims) từ Token
    public Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // 2. Kiểm tra Token có hợp lệ hay không
    public boolean validateToken(String authToken) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(authToken);
            return true;
        } catch (Exception ex) {
            // Có thể log lỗi ra đây (Hết hạn, sai chữ ký, token hỏng...)
            System.out.println("Invalid JWT Token: " + ex.getMessage());
        }
        return false;
    }
}