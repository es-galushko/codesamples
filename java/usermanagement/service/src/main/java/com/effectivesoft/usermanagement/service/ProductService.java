package com.effectivesoft.usermanagement.service;

import com.effectivesoft.usermanagement.entity.Product;

import java.util.List;

public interface ProductService {
    List<Product> findAll();
    Product findOne(Integer id);
    Product findByName(String name);
}
