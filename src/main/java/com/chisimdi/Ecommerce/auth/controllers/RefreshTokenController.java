package com.chisimdi.Ecommerce.auth.controllers;

import com.chisimdi.Ecommerce.auth.services.RefreshTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
public class RefreshTokenController {
    private RefreshTokenService refreshTokenService;

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response){
        return refreshTokenService.refreshToken(request,response);
    }

    @PostMapping("/logout")
    public ResponseEntity<?>logOut(HttpServletRequest request,HttpServletResponse response){
        return refreshTokenService.logout(request, response);
    }
}
