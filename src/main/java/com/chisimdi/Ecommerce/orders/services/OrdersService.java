package com.chisimdi.Ecommerce.orders.services;

import com.chisimdi.Ecommerce.auth.utils.CustomUserPrincipal;
import com.chisimdi.Ecommerce.exceptions.ConflictException;
import com.chisimdi.Ecommerce.exceptions.ResourceNotFoundException;
import com.chisimdi.Ecommerce.orders.mappers.OrdersMapper;
import com.chisimdi.Ecommerce.orders.models.Orders;
import com.chisimdi.Ecommerce.orders.models.OrdersDTO;
import com.chisimdi.Ecommerce.orders.models.OrdersIdempotency;
import com.chisimdi.Ecommerce.orders.repositories.OrdersIdempotencyRepository;
import com.chisimdi.Ecommerce.orders.repositories.OrdersRepository;
import com.chisimdi.Ecommerce.products.models.Products;
import com.chisimdi.Ecommerce.products.repositories.ProductRepository;
import com.chisimdi.Ecommerce.shopping.cart.models.CartStatus;
import com.chisimdi.Ecommerce.shopping.cart.models.ShoppingCart;
import com.chisimdi.Ecommerce.shopping.cart.repositories.ShoppingCartRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class OrdersService {
    private OrdersIdempotencyRepository ordersIdempotencyRepository;
    private ShoppingCartRepository shoppingCartRepository;
    private ProductRepository productRepository;
    private OrdersRepository ordersRepository;
    private OrdersMapper ordersMapper;

    @Transactional
    public OrdersDTO makeAnOrder(int shoppingCartId,String idempotencyKey){
        OrdersIdempotency idempotency= ordersIdempotencyRepository.findByIdempotencyKey(idempotencyKey);
        if(idempotency!=null){
            return ordersMapper.toDTO(idempotency.getOrders());
        }

        CustomUserPrincipal customUserPrincipal=(CustomUserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int userId=customUserPrincipal.getUserId();
        ShoppingCart shoppingCart=shoppingCartRepository.findByUsersIdAndId(userId,shoppingCartId).orElseThrow(()->new ResourceNotFoundException("Shopping cart with id "+shoppingCartId+" and userId "+userId+" not found"));
        Products products=shoppingCart.getProducts();
        if(products.getStock()<shoppingCart.getAmount()){
            throw new ConflictException("Amount exceeded stock");
        }
        products.setStock(products.getStock()-shoppingCart.getAmount());
        Orders orders=new Orders();
        orders.setShoppingCart(shoppingCart);
        shoppingCart.setCartStatus(CartStatus.ORDERED);

        OrdersIdempotency ordersIdempotency=new OrdersIdempotency();
        ordersIdempotency.setOrders(orders);
        ordersIdempotency.setIdempotencyKey(idempotencyKey);
        ordersIdempotencyRepository.save(ordersIdempotency);

        ordersRepository.save(orders);
        shoppingCartRepository.save(shoppingCart);
        productRepository.save(products);

        return ordersMapper.toDTO(orders);

    }

    public List<OrdersDTO>viewOrdersByUserId(int pageNumber,int size){
        CustomUserPrincipal customUserPrincipal=(CustomUserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int userId=customUserPrincipal.getUserId();

        Page<Orders>orders=ordersRepository.findByShoppingCartUsersId(userId, PageRequest.of(pageNumber, size));
        return ordersMapper.toDTOList(orders.getContent());
    }
}
