package ass.ass.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import ass.ass.DAO.ProductDAO;
import ass.ass.model.Product;

@Service
public class TrangchuService {
    private final ProductDAO productDAO;

    public TrangchuService(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    // Lấy tất cả sản phẩm
    public List<Product> getAllProducts() {
        return productDAO.findAll();
    }

    // Lọc sản phẩm theo danh mục
    public List<Product> getProductsByCategory(String category) {
        if (category != null && !category.isEmpty()) {
            return productDAO.findByCategory_Name(category);
        }
        return productDAO.findAll();
    }

    // Lấy danh sách thể loại + số lượng sản phẩm
    public Map<String, Long> getCategoriesWithProductCount() {
        List<Object[]> categoryData = productDAO.findCategoriesWithProductCount();
        Map<String, Long> categories = new LinkedHashMap<>();
        for (Object[] obj : categoryData) {
            categories.put((String) obj[0], (Long) obj[1]);
        }
        return categories;
    }

    // Lấy sản phẩm theo ID
    public Optional<Product> getProductById(Long id) {
        return productDAO.findById(id);
    }

    public List<Product> searchProducts(String keyword) {
        return productDAO.findByNameContainingIgnoreCase(keyword);
    }
}
