package ass.ass.DAO;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ass.ass.model.Product;

public interface ProductDAO extends JpaRepository<Product, Long> {
    List<Product> findByCategory_Id(Long categoryId);

    List<Product> findByNameContainingIgnoreCase(String keyword);

    @Query("SELECT p.category.name, COUNT(p) FROM Product p GROUP BY p.category.name")
    List<Object[]> findCategoriesWithProductCount();

    List<Product> findByCategory_Name(String categoryName);

}