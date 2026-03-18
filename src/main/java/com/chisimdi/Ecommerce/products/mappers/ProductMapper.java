package com.chisimdi.Ecommerce.products.mappers;

import com.chisimdi.Ecommerce.products.models.Products;
import com.chisimdi.Ecommerce.products.models.ProductsDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductsDTO toDTO(Products products);
    List<ProductsDTO>toDTOList(List<Products>products);
}
