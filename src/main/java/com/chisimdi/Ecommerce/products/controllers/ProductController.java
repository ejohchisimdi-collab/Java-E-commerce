package com.chisimdi.Ecommerce.products.controllers;

import com.chisimdi.Ecommerce.products.models.Products;
import com.chisimdi.Ecommerce.products.models.ProductsDTO;
import com.chisimdi.Ecommerce.products.services.ProductService;
import com.chisimdi.Ecommerce.products.utils.AddProductsUtil;
import com.chisimdi.Ecommerce.products.utils.EditProductsUtil;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private ProductService productService;

    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    @PostMapping("/")
    public ProductsDTO addProducts(@RequestBody @Valid AddProductsUtil util){
        return productService.addProduct(util);
    }

    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    @PutMapping("/")
    public ProductsDTO editProducts(@RequestBody @Valid EditProductsUtil util){
        return productService.editProduct(util);
    }

    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    @DeleteMapping("/{productId}")
    public String deleteProduct(@PathVariable int productId){
        return productService.deleteProduct(productId);
    }

    @GetMapping("/")
    public List<ProductsDTO>findAllProducts(@RequestParam(defaultValue = "0")int pageNumber,@RequestParam(defaultValue = "10") int size){
        return productService.findAllProducts(pageNumber, size);
    }
}
