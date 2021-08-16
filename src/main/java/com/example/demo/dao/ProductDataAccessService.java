package com.example.demo.dao;

import com.example.demo.model.Product;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository("fakeDao")
public class ProductDataAccessService implements ProductDao {

    private static List<Product> DB = new ArrayList<>();

    @Override
    public int insertProduct(UUID id, Product product, String createdAt) {

        System.out.println(product.toJson());

        DB.add(new Product(id, product.getName(), product.getDescription(), product.getBrand(), product.getTags(), product.getCategory(), product.getCreatedAt()));
        return 1;
    }

    @Override
    public List<Product> selectAllProducts() {
        return DB;
    }
}
