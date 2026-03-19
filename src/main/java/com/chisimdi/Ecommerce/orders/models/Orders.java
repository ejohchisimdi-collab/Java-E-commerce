package com.chisimdi.Ecommerce.orders.models;

import com.chisimdi.Ecommerce.shopping.cart.models.ShoppingCart;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
@Getter
@Setter
@Entity
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    private ShoppingCart shoppingCart;
    private LocalDate createdAt=LocalDate.now();
    @OneToMany(mappedBy = "orders",cascade = CascadeType.ALL)
    private List<OrdersIdempotency>ordersIdempotencies;
}
