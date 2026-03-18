package com.chisimdi.Ecommerce.products.repositories;

import com.chisimdi.Ecommerce.products.models.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Products,Integer> {

}
