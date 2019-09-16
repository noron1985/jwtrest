package com.ronald.jwtrest.api.controller;

import com.ronald.jwtrest.api.dto.CreateProductCommand;
import com.ronald.jwtrest.api.dto.ProductDto;
import com.ronald.jwtrest.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    //TODO "get /api/products create

    @GetMapping("/api/products")
    public List<ProductDto> findAll() {
        return productService.findAll();
    }

    @PostMapping("/api/product")
    public String create(@RequestBody CreateProductCommand command) {
        return productService.handleCreateCommand(command);
    }

    @GetMapping("/api/products/{id}")
    public ProductDto getProduct(@PathVariable String id) {
        return productService.findById(id);
    }

}
