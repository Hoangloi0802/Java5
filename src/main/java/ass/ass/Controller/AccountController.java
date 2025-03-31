package ass.ass.Controller;

import ass.ass.model.Account;
import ass.ass.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("admin/accounts")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @GetMapping
    public String getAccounts(Model model) {
        model.addAttribute("accounts", accountService.getAllAccounts());
        model.addAttribute("account", new Account());
        model.addAttribute("activePage", "account");
        return "Controller/QLtaikhoan";
    }


    @GetMapping("/edit/{id}")
    public String editAccount(@PathVariable Integer id, Model model) {
        accountService.getAccountById(id).ifPresent(account -> model.addAttribute("account", account));
        model.addAttribute("accounts", accountService.getAllAccounts());
        return "Controller/QLtaikhoan";
    }

    @PostMapping("/update")
    public String updateAccount(@ModelAttribute Account account, @RequestParam("imageFile") MultipartFile file) {
        try {
            accountService.updateAccount(account, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/admin/accounts";
    }

    @GetMapping("/delete/{id}")
    public String deleteAccount(@PathVariable Integer id) {
        accountService.deleteAccount(id);
        return "redirect:/admin/accounts";
    }
}
