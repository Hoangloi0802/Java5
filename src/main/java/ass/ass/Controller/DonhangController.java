package ass.ass.Controller;

import ass.ass.model.Order;
import ass.ass.service.DonhangService;
import ass.ass.DAO.AccountDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("admin/order")
public class DonhangController {
    @Autowired
    private DonhangService orderService;

    @Autowired
    private AccountDAO accountRepo;

    @GetMapping
    public String listOrders(Model model) {
        model.addAttribute("orders", orderService.getAllOrders());
        model.addAttribute("accounts", accountRepo.findAll());
        model.addAttribute("order", new Order());
        model.addAttribute("activePage", "order");
        return "Controller/QLdonhang"; 
    }

    @PostMapping("/save")
    public String saveOrder(@ModelAttribute Order order) {
        orderService.saveOrder(order);
        return "redirect:/admin/order";
    }

    @GetMapping("/edit/{id}")
    public String editOrder(@PathVariable Long id, Model model) {
        model.addAttribute("order", orderService.getOrderById(id));
        model.addAttribute("orders", orderService.getAllOrders());
        model.addAttribute("accounts", accountRepo.findAll());
        return "Controller/QLdonhang";
    }

    @PostMapping("/update")
    public String updateOrder(@ModelAttribute Order order) {
        orderService.saveOrder(order);
        return "redirect:/admin/order";
    }

    @GetMapping("/delete/{id}")
    public String deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return "redirect:/admin/order";
    }
}
