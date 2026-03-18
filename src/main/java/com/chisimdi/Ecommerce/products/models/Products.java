package com.chisimdi.Ecommerce.products.models;

import com.chisimdi.Ecommerce.shopping.cart.models.ShoppingCart;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Products {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(unique = true)
    private String name;
    private String description;
    @Column(precision = 16,scale = 4)
    private BigDecimal price;
    private int stock;
    private String category;
    @OneToMany(mappedBy = "products",cascade = CascadeType.ALL)
    private List<ShoppingCart>shoppingCarts=new ArrayList<>();
    @Version
    private Integer version;
}
