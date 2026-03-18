package com.chisimdi.Ecommerce.shopping.cart.repositories;

import com.chisimdi.Ecommerce.shopping.cart.models.ShoppingCart;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart,Integer> {
    Page<ShoppingCart>findByUsersId(int userId, Pageable pageable);
    Optional<ShoppingCart> findByUsersIdAndId(int userId, int cartId);
}
