package com.chisimdi.Ecommerce.user.services;

import com.chisimdi.Ecommerce.auth.models.RefreshToken;
import com.chisimdi.Ecommerce.auth.repositories.RefreshTokenRepository;
import com.chisimdi.Ecommerce.auth.services.JwtUtilService;
import com.chisimdi.Ecommerce.auth.utils.CustomUserPrincipal;
import com.chisimdi.Ecommerce.exceptions.InvalidCredentialsException;
import com.chisimdi.Ecommerce.exceptions.ResourceNotFoundException;
import com.chisimdi.Ecommerce.user.mappers.UserMapper;
import com.chisimdi.Ecommerce.user.models.UserDTO;
import com.chisimdi.Ecommerce.user.models.Users;
import com.chisimdi.Ecommerce.user.repositories.UserRepository;
import com.chisimdi.Ecommerce.user.utils.LoginResponse;
import com.chisimdi.Ecommerce.user.utils.LoginUtil;
import com.chisimdi.Ecommerce.user.utils.RegisterUserUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@AllArgsConstructor
public class UserService {
    private UserRepository userRepository;
    private UserMapper userMapper;
    private BCryptPasswordEncoder passwordEncoder;
    private JwtUtilService jwtUtilService;
    private RefreshTokenRepository refreshTokenRepository;

    public UserDTO registerUser(RegisterUserUtil util){
        Users users=new Users();
        users.setRole(util.getRole());
        users.setEmail(util.getEmail());
        users.setPassword(passwordEncoder.encode(util.getPassword()));
        users.setName(util.getName());
        userRepository.save(users);
        return userMapper.toDTO(users);
    }

    public LoginResponse logIn(LoginUtil util, HttpServletResponse response){
        Users user= userRepository.findByEmail(util.getEmail()).orElseThrow(()->new ResourceNotFoundException("User with email "+util.getEmail()+" not found"));

        if(!passwordEncoder.matches(util.getPassword(),user.getPassword())){
            throw new InvalidCredentialsException("Invalid password");
        }
        String accessToken = jwtUtilService.generateToken(user.getEmail(), user.getId(), user.getRole());

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setUserName(jwtUtilService.extractUserName(accessToken));
        loginResponse.setUserId(jwtUtilService.extractUserId(accessToken));
        loginResponse.setRole(jwtUtilService.extractRole(accessToken));
        loginResponse.setExpiration(jwtUtilService.extractExpiration(accessToken));
        loginResponse.setToken(accessToken);

        // --- Generate Refresh Token ---
        String refreshToken = jwtUtilService.generateRefreshToken(user.getEmail(), user.getId(), user.getRole());

        RefreshToken refreshTokenEntity = new RefreshToken();
        refreshTokenEntity.setUserId(user.getId());
        refreshTokenEntity.setUserName(user.getEmail());
        refreshTokenEntity.setToken(refreshToken);
        refreshTokenEntity.setRoles(user.getRole());
        refreshTokenEntity.setExpiration(jwtUtilService.extractExpiration(refreshToken)); // centralized expiration
        refreshTokenRepository.save(refreshTokenEntity);

        // --- Set HttpOnly Cookie ---
        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .sameSite("Strict")
                .maxAge(Duration.ofDays(30))
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());


        return loginResponse;

    }

    public UserDTO viewProfile(){
        CustomUserPrincipal customUserPrincipal=(CustomUserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int userId= customUserPrincipal.getUserId();

        Users user=userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("user with id "+userId+" not found"));
        return userMapper.toDTO(user);
    }

}
