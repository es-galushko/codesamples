package com.effectivesoft.usermanagement.dao;

import com.effectivesoft.usermanagement.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository  extends JpaRepository<Product, Integer> {
    Product findByName(String name);
}
