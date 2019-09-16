package com.ronald.jwtrest.service;

import com.ronald.jwtrest.api.dto.CreateProductCommand;
import com.ronald.jwtrest.api.dto.ProductDto;
import com.ronald.jwtrest.dao.ProductDao;
import com.ronald.jwtrest.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ProductService implements Function<Product, ProductDto> {

    @Autowired
    private ProductDao productDao;


    public ProductDto save(Product product) {
        return apply(productDao.save(product));
    }

    public List<ProductDto> findAll() {
        return productDao.findAll()
                .stream()
                .map(this::apply)
                .collect(Collectors.toList());
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

    public String handleCreateCommand(CreateProductCommand command) {
        return save(
                new Product(
                        UUID.randomUUID().toString(),
                        command.getNom(),
                        command.getPrice()))
                .getId();
    }

    @Override
    public ProductDto apply(Product product) {
        return new ProductDto(product.getId(), product.getName(), product.getPrice());
    }
}
