package com.chisimdi.Ecommerce.orders.models;

import com.chisimdi.Ecommerce.shopping.cart.models.ShoppingCart;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class OrdersDTO {
    private int id;
    private int shoppingCartId;
    private LocalDate createdAt=LocalDate.now();
}
