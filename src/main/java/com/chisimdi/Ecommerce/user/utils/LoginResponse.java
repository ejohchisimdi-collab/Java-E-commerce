package com.chisimdi.Ecommerce.user.utils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.Date;
@Getter
@Setter
public class LoginResponse {
    private String userName;
    private int userId;
    private Date expiration;
    private String role;
    private String token;
}
