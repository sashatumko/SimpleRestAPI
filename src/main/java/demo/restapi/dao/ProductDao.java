package demo.restapi.dao;

import demo.restapi.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductDao extends JpaRepository<Product, Long> {

    Page<Product> findByCategory(String category, Pageable pageable);

}
