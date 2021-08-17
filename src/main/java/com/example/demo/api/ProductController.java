package com.example.demo.api;

import com.example.demo.dao.ProductDao;
import com.example.demo.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/products")
public class ProductController {

    @Autowired
    ProductDao productDao;

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        try {
            String time = ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT);
            product.setCreatedAt(time);
            Product _product = productDao.save(new Product(product.getId(), product.getName(), product.getDescription(), product.getBrand(), product.getTags(), product.getCategory(), product.getCreatedAt()));
            return new ResponseEntity<>(_product, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        try {
            List<Product> products = new ArrayList<Product>();
            productDao.findAll().forEach(products::add);

            if (products.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(products, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // /v1/products/{category}?page=1&max=25
    // /v1/products/shirt?page=0&max=2
    @GetMapping("/{category}")
    public ResponseEntity<Map<String, Object>> findByCategory(
            @PathVariable("category") String category,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "max", required = false, defaultValue = "3") int max
    ) {
        try {
            //System.out.println("category: " + category + " page: " + page + " max: " + max);
            List<Product> products = new ArrayList<Product>();
            Pageable paging = PageRequest.of(page, max);

            Page<Product> pageTuts = productDao.findByCategory(category, paging);
            products = pageTuts.getContent();

            Map<String, Object> response = new HashMap<>();
            response.put("products", products);
            response.put("currentPage", pageTuts.getNumber());
            response.put("totalItems", pageTuts.getTotalElements());
            response.put("totalPages", pageTuts.getTotalPages());

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}