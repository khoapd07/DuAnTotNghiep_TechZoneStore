package com.poly.backend.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
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

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. Tắt CSRF (Vì ta dùng API REST stateless, không dùng Session cookie kiểu cũ)
                .csrf(AbstractHttpConfigurer::disable)

                // 2. Kích hoạt CORS (Để Vue.js port 5173 gọi sang Java port 8080 được)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // 3 PHÂN QUYỀN API
                .authorizeHttpRequests(auth -> auth
                        // 1. Cho phép truy cập không cần token: Đăng nhập, đăng ký, xem sản phẩm...
                        .requestMatchers("/api/auth/**", "/api/product/**", "/api/categories/**", "/api/brands/**", "/api/cart/**", "/api/reviews/**", "/api/orders/**").permitAll()

//                        .requestMatchers("/api/cart").authenticated()

                        // 2. KHÓA CHẶT API ADMIN: Bắt buộc user phải có Authority là "Admin" (tên role trong DB của bạn)
                        .requestMatchers("/api/admin/**").hasAuthority("Admin")

                        // 3. Các request còn lại (giỏ hàng, mua hàng của User) yêu cầu phải có Token hợp lệ
                        .anyRequest().authenticated()
                );
        // Chèn JwtAuthenticationFilter vào trước lớp Filter xác thực mặc định của Spring
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

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