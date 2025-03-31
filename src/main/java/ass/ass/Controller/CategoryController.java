package ass.ass.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import ass.ass.model.Category;
import ass.ass.service.CategoryService;

@Controller
@RequestMapping("admin/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public String viewCategories(Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("category", new Category());
        model.addAttribute("activePage", "category"); 
        return "Controller/QLloaihang";
    }

    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable("id") Integer id) {
        categoryService.deleteCategory(id);
        return "redirect:/admin/category";
    }

    @PostMapping("/save")
    public String saveCategory(@ModelAttribute Category category) {
        categoryService.saveCategory(category);
        return "redirect:/admin/category";
    }
}
