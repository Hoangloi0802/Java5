package ass.ass.Controller;

import ass.ass.model.Product;
import ass.ass.service.ProductService;
import ass.ass.DAO.CategoryDAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("admin/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryDAO categoryRepo;

    @GetMapping
    public String listProducts(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("categories", categoryRepo.findAll());
        model.addAttribute("product", new Product());
        model.addAttribute("activePage", "product");
        return "Controller/QLsanpham";
    }

    @PostMapping("/save")
    public String saveProduct(@ModelAttribute Product product, @RequestParam("imageFile") MultipartFile file) {
        productService.saveProduct(product, file);
        return "redirect:/admin/products";
    }

    @GetMapping("/edit/{id}")
    public String editProduct(@PathVariable Long id, Model model) {
        model.addAttribute("product", productService.getProductById(id).orElse(new Product()));
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("categories", categoryRepo.findAll());
        return "Controller/QLsanpham";
    }

    @PostMapping("/update")
    public String updateProduct(@ModelAttribute Product product, @RequestParam("imageFile") MultipartFile file) {
        productService.updateProduct(product, file);
        return "redirect:/admin/products";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/admin/products";
    }
}
