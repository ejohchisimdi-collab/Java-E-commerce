package com.chisimdi.Ecommerce.shopping.cart.repositories;

import com.chisimdi.Ecommerce.shopping.cart.models.ShoppingCartIdempotency;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingCartIdempotencyRepository extends JpaRepository<ShoppingCartIdempotency,Integer> {
    ShoppingCartIdempotency findByIdempotencyKey(String idempotencyKey);
}
