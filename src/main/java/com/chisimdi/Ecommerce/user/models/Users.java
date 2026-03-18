package com.chisimdi.Ecommerce.user.models;

import com.chisimdi.Ecommerce.shopping.cart.models.ShoppingCart;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(unique = true)
    private String email;
    private String name;
    private String role;
    private String password;
    @OneToMany(mappedBy = "users",cascade = CascadeType.ALL)
    private List<ShoppingCart>shoppingCarts=new ArrayList<>();
}
