package com.chisimdi.Ecommerce.orders.repositories;

import com.chisimdi.Ecommerce.orders.models.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdersRepository extends JpaRepository<Orders,Integer> {
    Page<Orders>findByShoppingCartUsersId(int userId, Pageable pageable);
}
