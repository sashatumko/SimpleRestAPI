package com.example.demo.dao;

import com.example.demo.model.Product;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

public interface ProductDao  {

    int insertProduct(UUID id, Product product, String createdAt);

    default int insertProduct(Product product) {
        // generate ID
        UUID id = UUID.randomUUID();
        product.setId(id);

        // generate created_at
        String time = ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT);
        product.setCreatedAt(time);

        return insertProduct(id, product, time);
    }

    List<Product> selectAllProducts();
}
