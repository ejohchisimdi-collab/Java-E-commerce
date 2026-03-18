package com.chisimdi.Ecommerce.shopping.cart.services;

import com.chisimdi.Ecommerce.auth.utils.CustomUserPrincipal;
import com.chisimdi.Ecommerce.exceptions.ConflictException;
import com.chisimdi.Ecommerce.exceptions.ResourceNotFoundException;
import com.chisimdi.Ecommerce.products.models.Products;
import com.chisimdi.Ecommerce.products.repositories.ProductRepository;
import com.chisimdi.Ecommerce.shopping.cart.mappers.ShoppingCartMapper;
import com.chisimdi.Ecommerce.shopping.cart.models.CartStatus;
import com.chisimdi.Ecommerce.shopping.cart.models.ShoppingCart;
import com.chisimdi.Ecommerce.shopping.cart.models.ShoppingCartDTO;
import com.chisimdi.Ecommerce.shopping.cart.models.ShoppingCartIdempotency;
import com.chisimdi.Ecommerce.shopping.cart.repositories.ShoppingCartIdempotencyRepository;
import com.chisimdi.Ecommerce.shopping.cart.repositories.ShoppingCartRepository;
import com.chisimdi.Ecommerce.user.models.Users;
import com.chisimdi.Ecommerce.user.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.apache.catalina.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@AllArgsConstructor
public class ShoppingCartService {
    private ShoppingCartRepository shoppingCartRepository;
    private ShoppingCartMapper shoppingCartMapper;
    private UserRepository userRepository;
    private ProductRepository productRepository;
    private ShoppingCartIdempotencyRepository idempotencyRepository;

    @Transactional
    public ShoppingCartDTO addToCart(int productId,int amount,String idempotencyKey){
        ShoppingCartIdempotency idempotency=idempotencyRepository.findByIdempotencyKey(idempotencyKey);
        if(idempotency!=null){
            return shoppingCartMapper.toDTO(idempotency.getShoppingCart());
        }

        CustomUserPrincipal customUserPrincipal=(CustomUserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int userId=customUserPrincipal.getUserId();

        Users user=userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User with id "+userId+" not found"));
        Products products=productRepository.findById(productId).orElseThrow(()->new ResourceNotFoundException("product with id "+productId+" not found"));

        if(products.getStock()<amount){
            throw  new ConflictException("Amount exceeded stock");
        }

        ShoppingCart shoppingCart=new ShoppingCart();
        shoppingCart.setCartStatus(CartStatus.PENDING);
        shoppingCart.setAmount(amount);
        shoppingCart.setPrice(products.getPrice().multiply(BigDecimal.valueOf(amount)));
        shoppingCart.setProducts(products);
        shoppingCart.setUsers(user);
        shoppingCartRepository.save(shoppingCart);

        ShoppingCartIdempotency shoppingCartIdempotency=new ShoppingCartIdempotency();
        shoppingCartIdempotency.setIdempotencyKey(idempotencyKey);
        shoppingCartIdempotency.setShoppingCart(shoppingCart);
        idempotencyRepository.save(shoppingCartIdempotency);

        return shoppingCartMapper.toDTO(shoppingCart);
    }

    public String deleteShoppingCart(int cartId){
        CustomUserPrincipal customUserPrincipal=(CustomUserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int userId=customUserPrincipal.getUserId();

        ShoppingCart shoppingCart= shoppingCartRepository.findByUsersIdAndId(userId,cartId).orElseThrow(()->new ResourceNotFoundException("Cart with userId "+userId+" and cartId "+cartId+" not found"));
        shoppingCartRepository.delete(shoppingCart);
        return "Shopping cart deleted";
    }

    public List<ShoppingCartDTO> viewCartByUserId(int pageNumber, int size){
        CustomUserPrincipal customUserPrincipal=(CustomUserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int userId=customUserPrincipal.getUserId();
        Page<ShoppingCart>shoppingCarts=shoppingCartRepository.findByUsersId(userId, PageRequest.of(pageNumber, size));
        return shoppingCartMapper.toDTOList(shoppingCarts.getContent());
    }

    public ShoppingCartDTO updateCart(int cartId, Integer amount){
        CustomUserPrincipal customUserPrincipal=(CustomUserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int userId=customUserPrincipal.getUserId();

        ShoppingCart shoppingCart= shoppingCartRepository.findByUsersIdAndId(userId,cartId).orElseThrow(()->new ResourceNotFoundException("Cart with userId "+userId+" and cartId "+cartId+" not found"));

        Products products=shoppingCart.getProducts();

        if(products.getStock()<amount){
            throw new ConflictException("Amount exceeded stock");
        }

        shoppingCart.setAmount(amount);
        shoppingCart.setPrice(BigDecimal.valueOf(amount).multiply(products.getPrice()));
        shoppingCartRepository.save(shoppingCart);
        return shoppingCartMapper.toDTO(shoppingCart);
    }
}
