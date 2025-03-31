package ass.ass.service;

import ass.ass.model.Product;
import ass.ass.DAO.ProductDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductDAO productRepo;

    private final String UPLOAD_DIR = "src/main/resources/static/images/";

    public List<Product> getAllProducts() {
        return productRepo.findAll();
    }

    public Optional<Product> getProductById(Long id) {
        return productRepo.findById(id);
    }

    public void saveProduct(Product product, MultipartFile file) {
        try {
            if (!file.isEmpty()) {
                String fileName = file.getOriginalFilename();
                Path uploadPath = Paths.get(UPLOAD_DIR);

                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                Path filePath = uploadPath.resolve(fileName);
                Files.write(filePath, file.getBytes());

                product.setImage(fileName);
            }
            productRepo.save(product);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateProduct(Product product, MultipartFile file) {
        try {
            Optional<Product> existingProductOpt = productRepo.findById(product.getId());

            if (existingProductOpt.isPresent()) {
                Product existingProduct = existingProductOpt.get();

                if (!file.isEmpty()) {
                    String fileName = file.getOriginalFilename();
                    Path filePath = Paths.get(UPLOAD_DIR + fileName);
                    Files.write(filePath, file.getBytes());

                    product.setImage(fileName);
                } else {
                    product.setImage(existingProduct.getImage());
                }

                productRepo.save(product);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteProduct(Long id) {
        productRepo.deleteById(id);
    }
}
