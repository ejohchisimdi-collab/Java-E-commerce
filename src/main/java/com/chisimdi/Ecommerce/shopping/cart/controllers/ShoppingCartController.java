package com.chisimdi.Ecommerce.shopping.cart.controllers;

import com.chisimdi.Ecommerce.shopping.cart.models.ShoppingCartDTO;
import com.chisimdi.Ecommerce.shopping.cart.services.ShoppingCartService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/cart")
public class ShoppingCartController {
    private ShoppingCartService shoppingCartService;

    @PostMapping("/")
    public ShoppingCartDTO addCart(@RequestParam int productId, @RequestParam int amount, @RequestHeader("Idempotency-Key")String idempotencyKey){
        return shoppingCartService.addToCart(productId, amount, idempotencyKey);
    }

    @DeleteMapping("/{cartId}")
    public String deleteCart(@PathVariable  int cartId){
        return shoppingCartService.deleteShoppingCart(cartId);
    }

    @GetMapping("/")
    public List<ShoppingCartDTO>viewCartByUserId(@RequestParam(defaultValue = "0")int pageNumber,@RequestParam(defaultValue = "10")int size){
        return shoppingCartService.viewCartByUserId(pageNumber, size);
    }

    @PutMapping("/")
    public ShoppingCartDTO updateCart(@RequestParam int cartId,@RequestParam int amount){
        return shoppingCartService.updateCart(cartId,amount);
    }
}
