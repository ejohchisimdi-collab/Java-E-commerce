package com.chisimdi.Ecommerce.orders.mappers;

import com.chisimdi.Ecommerce.orders.models.Orders;
import com.chisimdi.Ecommerce.orders.models.OrdersDTO;
import com.chisimdi.Ecommerce.shopping.cart.models.ShoppingCart;
import com.chisimdi.Ecommerce.shopping.cart.models.ShoppingCartDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrdersMapper {
    @Mapping(source = "shoppingCart.id",target = "shoppingCartId")
    OrdersDTO toDTO(Orders orders);
    List<OrdersDTO>toDTOList(List<Orders>orders);;
}
