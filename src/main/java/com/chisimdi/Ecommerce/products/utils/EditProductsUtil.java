package com.chisimdi.Ecommerce.products.utils;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class EditProductsUtil {

    @NotNull(message = "Product ID is required")
    @Min(value = 1, message = "Product ID must be valid")
    private Integer productId;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private BigDecimal price;

    @Min(value = 0, message = "Stock cannot be negative")
    private Integer stock;

    @Size(max = 100, message = "Category too long")
    private String category;
}