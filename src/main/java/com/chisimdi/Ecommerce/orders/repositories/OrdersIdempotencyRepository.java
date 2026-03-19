package com.chisimdi.Ecommerce.orders.repositories;

import com.chisimdi.Ecommerce.orders.models.OrdersIdempotency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdersIdempotencyRepository extends JpaRepository<OrdersIdempotency,Integer> {
    OrdersIdempotency findByIdempotencyKey(String idempotencyKey);
}

