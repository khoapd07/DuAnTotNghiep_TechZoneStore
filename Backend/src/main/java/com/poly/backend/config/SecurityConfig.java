package com.poly.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. Tắt CSRF (Vì ta dùng API REST stateless, không dùng Session cookie kiểu cũ)
                .csrf(AbstractHttpConfigurer::disable)

                // 2. Kích hoạt CORS (Để Vue.js port 5173 gọi sang Java port 8080 được)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // 3. Phân quyền: CHO PHÉP TẤT CẢ (Permit All)
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // Mở toang mọi endpoint
                );

        return http.build();
    }

    // Cấu hình CORS chi tiết
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Cho phép frontend từ localhost:5173 truy cập
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173"));

        // Cho phép các method: GET, POST, PUT, DELETE, v.v.
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // Cho phép mọi header (Authorization, Content-Type...)
        configuration.setAllowedHeaders(Arrays.asList("*"));

        // Cho phép gửi credentials (nếu sau này cần cookie/token)
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}