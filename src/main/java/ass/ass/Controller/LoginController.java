package ass.ass.Controller;

import ass.ass.model.Account;
import ass.ass.service.AuthService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class LoginController {

    @Autowired
    private AuthService accountService;

    @GetMapping("/login")
    public String loginPage() {
        return "login/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, HttpSession session,
            Model model) {
        Account user = accountService.login(username, password);

        if (user == null) {
            model.addAttribute("error", "Sai tài khoản hoặc mật khẩu!");
            return "login/login";
        }

        session.setAttribute("user", user);
        return "redirect:/";
    }

    @GetMapping("/doimk")
    public String changePasswordPage(HttpSession session, Model model) {
        Account user = (Account) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("account", user);
        return "login/doimatkhau";
    }

    @PostMapping("/doimk")
    public String changePassword(@RequestParam String currentPassword,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword,
            HttpSession session,
            Model model) {
        Account user = (Account) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        boolean success = accountService.changePassword(user, currentPassword, newPassword, confirmPassword);

        if (!success) {
            model.addAttribute("error", "Mật khẩu cũ không đúng hoặc mật khẩu mới không hợp lệ!");
            return "login/doimatkhau";
        }

        session.setAttribute("user", user);
        model.addAttribute("message", "Đổi mật khẩu thành công!");
        return "login/doimatkhau";
    }

    @GetMapping("/profile")
    public String profilePage(HttpSession session, Model model) {
        Account user = (Account) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("account", user);
        return "login/profiles";
    }

    @PostMapping("/profile/update")
    public String updateProfile(
            @RequestParam String fullname,
            @RequestParam String email,
            @RequestParam(required = false) MultipartFile imageFile,
            HttpSession session,
            Model model) {

        Account user = (Account) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        try {
            accountService.updateProfile(user, fullname, email, imageFile);

            session.setAttribute("user", user);

            model.addAttribute("message", "Cập nhật thành công!");
        } catch (IOException e) {
            model.addAttribute("error", "Lỗi khi tải ảnh lên: " + e.getMessage());
        }

        model.addAttribute("account", user);
        return "login/profiles";
    }

    @GetMapping("/dangky")
    public String dangky() {
        return "login/dangky";
    }

    @PostMapping("/dangky")
    public String register(@RequestParam String fullname,
            @RequestParam String email,
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String confirmPassword,
            HttpServletResponse response,
            Model model) throws MessagingException {

        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Mật khẩu và xác nhận mật khẩu không khớp!");
            return "login/dangky";
        }

        if (accountService.isUsernameExists(username)) {
            model.addAttribute("error", "Tên đăng nhập đã tồn tại");
            return "login/dangky";
        }
        accountService.registerUser(fullname, email, username, password, response);
        model.addAttribute("message", "Vui lòng kiểm tra email để kích hoạt tài khoản.");

        return "login/dangky";
    }

    @GetMapping("/activate")
    public String activate(HttpServletRequest request, Model model) {
        boolean success = accountService.activateUser(request);
        model.addAttribute("message", success ? "Tài khoản đã được kích hoạt!" : "Kích hoạt thất bại!");
        return "login/login";
    }

    @GetMapping("/forgot-password")
    public String forgotPasswordPage() {
        return "login/forgot-password"; 
    }

    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestParam String email, HttpServletResponse response, Model model)
            throws MessagingException {
        boolean success = accountService.forgotPassword(email, response);
        if (success) {
            model.addAttribute("message", "Vui lòng kiểm tra email để đặt lại mật khẩu.");
        } else {
            model.addAttribute("error", "Email không tồn tại trong hệ thống.");
        }
        return "login/forgot-password"; 
    }

    @GetMapping("/reset-password")
    public String resetPasswordPage() {
        return "login/reset"; 
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam String newPassword, HttpServletRequest request, Model model) {
        boolean success = accountService.resetPassword(request, newPassword);
        model.addAttribute("message", success ? "Mật khẩu đã được đặt lại!" : "Lỗi đặt lại mật khẩu!");
        return "login/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
