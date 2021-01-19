package com.effectivesoft.usermanagement.service.impl;

import com.effectivesoft.usermanagement.dao.ProductRepository;
import com.effectivesoft.usermanagement.entity.Product;
import com.effectivesoft.usermanagement.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Product findOne(Integer id) {
        return productRepository.findOne(id);
    }

    @Override
    public Product findByName(String name) {
        return productRepository.findByName(name);
    }
}
