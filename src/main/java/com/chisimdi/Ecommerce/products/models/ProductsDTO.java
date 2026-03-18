package com.chisimdi.Ecommerce.products.models;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter
public class ProductsDTO {
    private int id;
    private String name;
    private String description;
    private BigDecimal price;
    private int stock;
    private String category;
}
