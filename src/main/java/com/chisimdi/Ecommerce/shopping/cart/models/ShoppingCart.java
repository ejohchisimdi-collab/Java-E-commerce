package com.chisimdi.Ecommerce.shopping.cart.models;

import com.chisimdi.Ecommerce.products.models.Products;
import com.chisimdi.Ecommerce.user.models.Users;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.apache.catalina.User;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
public class ShoppingCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int amount;
    private BigDecimal price;
    @ManyToOne
    private Users users;
    @ManyToOne
    private Products products;
    @Enumerated(EnumType.STRING)
    private CartStatus cartStatus;

    @OneToMany(mappedBy = "shoppingCart",cascade = CascadeType.ALL)
    private List<ShoppingCartIdempotency>shoppingCartIdempotencies=new ArrayList<>();
}
