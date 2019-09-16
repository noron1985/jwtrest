package com.ronald.jwtrest.dao;

import com.ronald.jwtrest.model.Product;

import java.util.List;

public interface ProductDao {

    Product findById(String id);

    List<Product> findAll();

    Product delete(String id);

    Product save(Product product);
}
