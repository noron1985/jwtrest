package com.ronald.jwtrest.service;

import com.ronald.jwtrest.api.dto.ProductDto;
import com.ronald.jwtrest.dao.ProductDao;
import com.ronald.jwtrest.model.Product;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ProductService implements Function<Product, ProductDto> {


    @Autowired
    private
    ProductDao productDao;


    public ProductDto save(Product product) {
        return apply(productDao.save(product));
    }

    public List<ProductDto> findAll() {
        return productDao.findAll().stream().map(this::apply).collect(Collectors.toList());
    }

    public ProductDto findById(String id) {
        Product product = productDao.findById(id);
        if (product != null) {
            return apply(product);
        }
        //TODO create custom ProductionConflictException
        else {
            throw new RuntimeException("product not found");
        }
    }


    @Override
    public ProductDto apply(Product product) {
        return new ProductDto(product.getId(), product.getName(), product.getPrice());
    }
}
