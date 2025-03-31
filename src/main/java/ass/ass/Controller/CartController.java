package ass.ass.Controller;

import ass.ass.service.CartService;
import ass.ass.service.OrderService;
import jakarta.servlet.http.HttpSession;
import ass.ass.model.Account;
import ass.ass.model.Order;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("cart")
public class CartController {

    private final CartService cartService;
    private final OrderService orderService;

    public CartController(CartService cartService, OrderService orderService) {
        this.cartService = cartService;
        this.orderService = orderService;
    }

    @ModelAttribute("cartCount")
    public int getCartCount() {
        return cartService.getCartQuantities().values().stream().mapToInt(Integer::intValue).sum();
    }

    @GetMapping
    public String viewCart(Model model) {
        model.addAttribute("cartItems", cartService.getCartItems());
        model.addAttribute("cartQuantities", cartService.getCartQuantities());
        model.addAttribute("total", cartService.getTotalPrice());
        return "home/Cart";
    }

    @GetMapping("/add/{id}")
    public String addToCart(@PathVariable Long id) {
        cartService.addToCart(id);
        return "redirect:/cart";
    }

    @PostMapping("/update")
    public String updateCart(@RequestParam Long id, @RequestParam int quantity) {
        cartService.updateCart(id, quantity);
        return "redirect:/cart";
    }

    @PostMapping("/remove")
    public String removeFromCart(@RequestParam Long id) {
        cartService.removeFromCart(id);
        return "redirect:/cart";
    }

    @PostMapping("/clear")
    public String clearCart() {
        cartService.clearCart();
        return "redirect:/cart";
    }

    @PostMapping("/checkout")
    public String checkoutCart(HttpSession session) {
        Account account = (Account) session.getAttribute("user");
        if (account == null) {
            return "redirect:/login"; 
        }
    
        String username = account.getUsername(); 
        Map<Long, Integer> cartItems = cartService.getCartQuantities();
    
      
        Order order = orderService.createOrder(username, cartItems);
    
        cartService.clearCart();
    
        return "redirect:/order/success?orderId=" + order.getId();
    }
    
}
