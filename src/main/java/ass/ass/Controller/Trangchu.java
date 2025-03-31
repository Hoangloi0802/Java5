package ass.ass.Controller;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ass.ass.service.TrangchuService;
import ass.ass.service.CartService;
import ass.ass.model.Product;

@Controller
@RequestMapping("/")
public class Trangchu {
    private final TrangchuService trangchuService;
    private final CartService cartService;

    public Trangchu(TrangchuService trangchuService, CartService cartService) {
        this.trangchuService = trangchuService;
        this.cartService = cartService;
    }

    @GetMapping
    public String trangchu(@RequestParam(value = "category", required = false) String category, Model model) {
        model.addAttribute("products", trangchuService.getAllProducts());
        model.addAttribute("categories", trangchuService.getCategoriesWithProductCount()); 
        model.addAttribute("selectedCategory", category);
        return "home/index";
    }

    @GetMapping("shop")
    public String shop(@RequestParam(value = "category", required = false) String category, Model model) {
        model.addAttribute("products", trangchuService.getProductsByCategory(category));
        model.addAttribute("categories", trangchuService.getCategoriesWithProductCount()); 
        model.addAttribute("selectedCategory", category);
        return "home/shop";
    }

    @GetMapping("detail")
    public String detail() {
        return "home/detail";
    }

    @GetMapping("chitiet/{id}")
    public String getProductById(@PathVariable Long id, Model model) {
        Optional<Product> product = trangchuService.getProductById(id);
        if (product.isPresent()) {
            model.addAttribute("product", product.get());
            return "home/detail";
        }
        return "error-page";
    }

    @ModelAttribute("cartCount")
    public int getCartCount() {
        return cartService.getCartQuantities().values().stream().mapToInt(Integer::intValue).sum();
    }

    @GetMapping("search")
    public String searchProducts(@RequestParam("keyword") String keyword, Model model) {
        model.addAttribute("products", trangchuService.searchProducts(keyword));
        return "home/shop";
    }
}
