package ass.ass.Controller;

import ass.ass.model.Order;
import ass.ass.model.Account;
import ass.ass.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public String listOrders(Model model, HttpSession session) {
        // Lấy tài khoản từ session
        Account account = (Account) session.getAttribute("user");
        if (account == null) {
            return "redirect:/login"; 
        }

        String username = account.getUsername();
        List<Order> orders = orderService.getOrdersByUser(username);
        model.addAttribute("orders", orders);

        return "order/list"; 
    }

   
    @GetMapping("/{orderId}")
    public String orderDetail(@PathVariable Long orderId, Model model, HttpSession session) {
        Account account = (Account) session.getAttribute("user");
        if (account == null) {
            return "redirect:/login";
        }

        Order order = orderService.getOrderById(orderId);
        model.addAttribute("order", order);
        return "order/detail";
    }

    @GetMapping("/success")
    public String orderSuccess(@RequestParam Long orderId, Model model) {
        model.addAttribute("orderId", orderId);
        return "order/success"; 
    }
}
