package ass.ass.service;

import ass.ass.DAO.CategoryDAO;
import ass.ass.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryDAO categoryDAO;

    public List<Category> getAllCategories() {
        return categoryDAO.findAll();
    }

    public Optional<Category> getCategoryById(Integer id) {
        return categoryDAO.findById(id);
    }

    public void saveCategory(Category category) {
        categoryDAO.save(category);
    }

    public void deleteCategory(Integer id) {
        categoryDAO.findById(id).ifPresent(categoryDAO::delete);
    }
}
