package com.chisimdi.Ecommerce.products.services;

import com.chisimdi.Ecommerce.exceptions.ResourceNotFoundException;
import com.chisimdi.Ecommerce.products.mappers.ProductMapper;
import com.chisimdi.Ecommerce.products.models.Products;
import com.chisimdi.Ecommerce.products.models.ProductsDTO;
import com.chisimdi.Ecommerce.products.repositories.ProductRepository;
import com.chisimdi.Ecommerce.products.utils.AddProductsUtil;
import com.chisimdi.Ecommerce.products.utils.EditProductsUtil;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductService {
    ProductRepository productRepository;
    ProductMapper productMapper;

    public ProductsDTO addProduct(AddProductsUtil util){
        Products products=new Products();
        products.setCategory(util.getCategory());
        products.setDescription(util.getDescription());
        products.setStock(util.getStock());
        products.setName(util.getName());
        products.setPrice(util.getPrice());
        productRepository.save(products);
        return productMapper.toDTO(products);
    }

    public ProductsDTO editProduct(EditProductsUtil util){
        Products products=productRepository.findById(util.getProductId()).orElseThrow(()->new ResourceNotFoundException("product with id "+util.getProductId()+" not found"));
        if(util.getCategory()!=null){
            products.setCategory(util.getCategory());
        }
        if(util.getDescription()!=null){
            products.setDescription(util.getDescription());
        }
        if(util.getStock()!=null){
            products.setStock(util.getStock());
        }

        if(util.getPrice()!=null){
            products.setPrice(util.getPrice());
        }
        productRepository.save(products);
        return productMapper.toDTO(products);
    }

    public String deleteProduct(int productId){
        Products products=productRepository.findById(productId).orElseThrow(()->new ResourceNotFoundException("product with id "+productId+" not found"));
        productRepository.delete(products);
        return "Product Deleted";
    }

    public List<ProductsDTO>findAllProducts(int pageNumber,int size){
        Page<Products> products=productRepository.findAll(PageRequest.of(pageNumber, size));
        return productMapper.toDTOList(products.getContent());
    }
}
