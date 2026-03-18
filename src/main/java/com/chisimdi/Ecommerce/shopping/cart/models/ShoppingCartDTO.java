package com.chisimdi.Ecommerce.shopping.cart.models;

import com.chisimdi.Ecommerce.products.models.Products;
import com.chisimdi.Ecommerce.user.models.Users;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter
public class ShoppingCartDTO {
    private int id;
    private int amount;
    private BigDecimal price;
    private int userId;
    private String productName;
    private CartStatus cartStatus;
}
