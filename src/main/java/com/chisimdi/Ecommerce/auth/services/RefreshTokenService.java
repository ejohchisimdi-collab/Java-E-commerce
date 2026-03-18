package com.chisimdi.Ecommerce.auth.services;

import com.chisimdi.Ecommerce.auth.models.RefreshToken;
import com.chisimdi.Ecommerce.auth.repositories.RefreshTokenRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
@Service
@AllArgsConstructor
public class RefreshTokenService {
    private RefreshTokenRepository refreshTokenRepository;
    private JwtUtilService jwtUtilService;

    public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = Arrays.stream(Optional.ofNullable(request.getCookies()).orElse(new Cookie[0]))
                .filter(c -> c.getName().equals("refreshToken"))
                .map(Cookie::getValue)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No refresh token"));


        // Lookup in DB
        RefreshToken storedToken = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        if (storedToken.getRevoked().equals(true) || storedToken.getExpiration().before(new Date())) {
            throw new RuntimeException("Token expired or revoked");
        }

        // Rotate refresh token
        storedToken.setRevoked(true);
        refreshTokenRepository.save(storedToken);
        String userName = jwtUtilService.extractUserName(refreshToken);
        String role = jwtUtilService.extractRole(refreshToken);
        int userId = jwtUtilService.extractUserId(refreshToken);
        String newRefreshToken = jwtUtilService.generateRefreshToken(userName, userId, role);
        RefreshToken newTokenEntity = new RefreshToken();
        newTokenEntity.setToken(newRefreshToken);
        newTokenEntity.setUserId(userId);
        newTokenEntity.setExpiration(jwtUtilService.extractExpiration(newRefreshToken));
        refreshTokenRepository.save(newTokenEntity);

        // New access token
        String newAccessToken = jwtUtilService.generateToken(userName, userId, role);

        // Update HttpOnly cookie
        ResponseCookie cookie = ResponseCookie.from("refreshToken", newRefreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .sameSite("Strict")
                .maxAge(Duration.ofDays(30))
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok(Map.of("accessToken", newAccessToken,"expiration",jwtUtilService.extractExpiration(newAccessToken)));
    }

    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = Arrays.stream(Optional.ofNullable(request.getCookies()).orElse(new Cookie[0]))
                .filter(c -> c.getName().equals("refreshToken"))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);

        if (refreshToken != null) {
            refreshTokenRepository.findByToken(refreshToken).ifPresent(t -> {
                t.setRevoked(true);
                refreshTokenRepository.save(t);
            });
        }

        // Clear cookie
        ResponseCookie cookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok(Map.of("message", "Logged out"));
    }
}
