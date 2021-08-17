package restapi.api;

import restapi.dao.ProductDao;
import restapi.model.Product;
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
import java.net.URLDecoder;

@RestController
@RequestMapping("/v1/products")
public class ProductController {

    @Autowired
    ProductDao productDao;

    // insert a product into DB
    // endpoint: /v1/products
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        try {
            product.setCreatedAt(ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT));
            Product newProduct = productDao.save(product);
            return new ResponseEntity<>(newProduct, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // get all products from DB
    // endpoint: /v1/products
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
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // get products from DB by category
    // supports pagination, products returned sorted by creation time (newest to oldest)
    // /v1/products/{category}?page=1&max=25
    @GetMapping("/{category}")
    public ResponseEntity<Map<String, Object>> findByCategory(
            @PathVariable(required = true) String category,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "max", required = false, defaultValue = "3") int max
    ) {
        try {
            Pageable paging = PageRequest.of(page, max, Sort.by(Sort.Direction.DESC, "createdAt"));
            List<Product> products = new ArrayList<Product>();
            Page<Product> pageProducts;

            if(category == null) {
                pageProducts = productDao.findAll(paging);
            }
            else {
                String decodedCategory = URLDecoder.decode(category, "UTF-8");
                pageProducts = productDao.findByCategory(decodedCategory, paging);
            }

            products = pageProducts.getContent();

            if (products.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("products", products);
            response.put("current_page", pageProducts.getNumber());
            response.put("total_items", pageProducts.getTotalElements());
            response.put("total_pages", pageProducts.getTotalPages());

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}