package com.chisimdi.Ecommerce.shopping.cart.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ShoppingCartIdempotency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(unique = true)
    private String idempotencyKey;
    @ManyToOne
    private ShoppingCart shoppingCart;
}
