package demo.restapi.api;

import demo.restapi.dao.ProductDao;
import demo.restapi.model.Product;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/products")
public class ProductController {

    @Autowired
    ProductDao productDao;

    /**
     * Handling insertion of products (POST requests) at endpoint /v1/product
     * @param product object representing product parsed from request body
     * @return the inserted object in JSON format
     */
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        try {
            product.setCreatedAt(ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT));
            Product newProduct = productDao.save(product);
            return new ResponseEntity<>(newProduct, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Handling GET requests to endpoint /v1/products/{category}
     * @param category The desired category by which to search for products
     * @param page Optional parameter to specify which page to request (default 1)
     * @param max Optional parameter to specify a maximum number of products per page (default 3)
     * @param sort Optional parameter to specify a field to sort by (default createdAt)
     * @return list of products with exact category match sorted from newest to oldest
     */

    @GetMapping
    public ResponseEntity<Map<String, Object>> findByCategory(
            @RequestParam(name = "category", required = false, defaultValue = "all") String category,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "max", required = false, defaultValue = "3") int max,
            @RequestParam(name = "sort", required = false, defaultValue = "-createdAt") String sort
    ) {

        Pageable paging;
        Page<Product> pageProducts;
        Map<String, Object> response = new HashMap<>();

        try {
            Sort.Direction sortDir = (sort.charAt(0) == '+') ? Sort.Direction.ASC : Sort.Direction.DESC;
            paging = PageRequest.of(page - 1, max, Sort.by(sortDir, sort.substring(1)));

            if(category.equals("all")) {
                pageProducts = productDao.findAll(paging);
            }
            else {
                pageProducts = productDao.findByCategory(category, paging);
            }

            List<Product> products = pageProducts.getContent();
            if (products.isEmpty()) {
                return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
            } else {
                response.put("products", products);
                response.put("current_page", pageProducts.getNumber()+1);
                response.put("total_items", pageProducts.getTotalElements());
                response.put("total_pages", pageProducts.getTotalPages());
                return new ResponseEntity<>(response, HttpStatus.OK);
            }

        } catch (java.lang.IllegalArgumentException e) {
            response.put("message", "invalid query parameter");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (org.springframework.data.mapping.PropertyReferenceException e) {
            response.put("message", "invalid sort parameter");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            System.out.println("exception type " + e.getClass().getName());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}