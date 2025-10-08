package com.spring.test.restapi.config;

import com.spring.test.restapi.util.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.lang.NonNull;

import java.io.IOException;

@Configuration
public class SecurityConfig {
    private final JwtUtil jwtUtil;

    public SecurityConfig(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/users/login",
                                "/api/users/register",
                                "/", // 루트(index.html)
                                "/index.html",
                                "/todo", // 여기가 todo.html로 forward되는 경로
                                "/todo.html", // 실제 정적 파일도 허용해야 함
                                "/user.js",
                                "/todo.js",
                                "/todo.css",
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/api-docs/**",
                                "/h2-console/**")
                        .permitAll()
                        .anyRequest().authenticated())
                .exceptionHandling(eh -> eh
                        .authenticationEntryPoint(customEntryPoint()))
                .addFilterBefore(new JwtAuthFilter(jwtUtil),
                        org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // 인증 실패 시 index.html로 리다이렉트 + 메시지 전달
    private AuthenticationEntryPoint customEntryPoint() {
        return (request, response, authException) -> {
            response.setContentType("text/html; charset=UTF-8");
            response.getWriter().write(
                    "<script>alert('회원가입을 해주세요'); location.href='/';</script>");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        };
    }

    // JWT 인증 필터: 모든 요청에서 JWT 토큰을 검사하여 인증 처리
    public static class JwtAuthFilter extends OncePerRequestFilter {
        // JwtUtil 의존성 주입 (토큰 검증/파싱용)
        private final JwtUtil jwtUtil;

        public JwtAuthFilter(JwtUtil jwtUtil) {
            this.jwtUtil = jwtUtil;
        }

        @Override
        protected void doFilterInternal(
                @NonNull HttpServletRequest request,
                @NonNull HttpServletResponse response,
                @NonNull FilterChain filterChain)
                throws ServletException, IOException {
            // 1. Authorization 헤더에서 Bearer 토큰 추출
            String authHeader = request.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7); // "Bearer " 이후 토큰만 추출

                // 2. 토큰 유효성 검증
                if (jwtUtil.validateToken(token)) {
                    // 3. 토큰에서 username 추출
                    String username = jwtUtil.getUsernameFromToken(token);

                    // 4. 인증 객체 생성 (권한 정보는 null)
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            username, null, null);

                    // 5. 인증 객체에 요청 정보 추가
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // 6. SecurityContext에 인증 정보 저장 (이후 컨트롤러에서 인증된 사용자로 인식)
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
            // 7. 다음 필터(혹은 컨트롤러)로 요청 전달
            filterChain.doFilter(request, response);
        }
    }
}
