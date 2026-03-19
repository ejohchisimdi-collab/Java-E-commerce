package com.chisimdi.Ecommerce.orders.controller;

import com.chisimdi.Ecommerce.orders.models.OrdersDTO;
import com.chisimdi.Ecommerce.orders.services.OrdersService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@AllArgsConstructor
public class OrdersController {
    private OrdersService ordersService;

    @PostMapping("/{cartId}")
    public OrdersDTO makeAnOrder(@RequestHeader("Idempotency-Key")String idempotencyKey, @PathVariable int cartId){
        return ordersService.makeAnOrder(cartId,idempotencyKey);
    }

    @GetMapping("/")
    public List<OrdersDTO>getOrdersByUserId(@RequestParam(defaultValue = "0")int pageNumber,@RequestParam(defaultValue = "10")int size){
        return ordersService.viewOrdersByUserId(pageNumber, size);
    }
}
