package com.chisimdi.Ecommerce.shopping.cart.mappers;

import com.chisimdi.Ecommerce.shopping.cart.models.ShoppingCart;
import com.chisimdi.Ecommerce.shopping.cart.models.ShoppingCartDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ShoppingCartMapper {
    @Mapping(source = "shoppingCart.users.id",target = "userId")
    @Mapping(source = "shoppingCart.products.name",target = "productName")
    ShoppingCartDTO toDTO(ShoppingCart shoppingCart);
    List<ShoppingCartDTO>toDTOList(List<ShoppingCart>shoppingCarts);
}
