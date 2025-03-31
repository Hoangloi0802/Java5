package ass.ass.DAO;



import org.springframework.data.jpa.repository.JpaRepository;

import ass.ass.model.Category;

public interface CategoryDAO extends JpaRepository<Category, Integer>{
    
}