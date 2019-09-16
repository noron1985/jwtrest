package com.ronald.jwtrest.dao.impl;

import com.ronald.jwtrest.dao.ProductDao;
import com.ronald.jwtrest.model.Product;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProductDaoImpl implements ProductDao {

    List<Product> products = new ArrayList<>();


    @Override
    public Product findById(String id) {
        return products.stream().filter(product -> product.getId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public List<Product> findAll() {
        return products;
    }

    @Override
    public Product delete(String id) {
        Product toDelete = findById(id);
        if (toDelete == null) {
            products.remove(toDelete);
        }

        return findById(toDelete.getId());
    }

    @Override
    public Product save(Product product) {
        if (findById(product.getId()) == null) {
            products.add(product);
        } else {
            products.remove(product);
            products.add(product);
        }
        return findById(product.getId());
    }

    @PostConstruct
    public void onPostConstruct() {
        products.add(
                new Product("AA0001", "Smartphone", 199.99));
        products.add(
                new Product("AA0002", "Souris", 10.99));

    }
}
