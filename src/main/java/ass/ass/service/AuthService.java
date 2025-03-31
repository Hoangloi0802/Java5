package ass.ass.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import ass.ass.DAO.AccountDAO;
import ass.ass.model.Account;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class AuthService {
    @Autowired
    private AccountDAO accountDAO;
    @Autowired
    private CookieService cookieService;

    @Autowired
    private MailService mailService;

    private static final String IMAGE_DIR = "src/main/resources/static/images/";

    public Account login(String username, String password) {
        Account user = accountDAO.findByUsername(username);
        if (user != null && password.equals(user.getPassword())) {
            return user;
        }
        return null;
    }

    public boolean changePassword(Account account, String currentPassword, String newPassword, String confirmPassword) {
        if (!account.getPassword().equals(currentPassword)) {
            return false;
        }

        if (!newPassword.equals(confirmPassword)) {
            return false;
        }

        account.setPassword(newPassword);
        accountDAO.save(account);
        return true;
    }

    public void updateProfile(Account account, String fullname, String email, MultipartFile imageFile)
            throws IOException {
        account.setFullname(fullname);
        account.setEmail(email);

        if (imageFile != null && !imageFile.isEmpty()) {
            String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
            Path uploadPath = Paths.get(IMAGE_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            Path filePath = uploadPath.resolve(fileName);
            Files.write(filePath, imageFile.getBytes());
            account.setPhoto(fileName);
        }

        accountDAO.save(account);
    }

    public Account registerUser(String fullname, String email, String username, String password,
            HttpServletResponse response) throws MessagingException {
        Account user = new Account();
        user.setFullname(fullname);
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(password);
        user.setActivated(false);
        accountDAO.save(user);

        // Lưu thông tin vào cookie
        String data = user.getId() + ":" + user.getEmail();
        cookieService.setCookie(response, "activation", data, 60 * 60); // 1 giờ

        // Gửi email kích hoạt
        String activationLink = "http://localhost:8080/activate";
        String body = "<p>Chào " + fullname + ",</p>"
                + "<p>Vui lòng bấm vào link dưới đây để kích hoạt tài khoản:</p>"
                + "<p><a href='" + activationLink + "'>Kích hoạt tài khoản</a></p>";
        mailService.sendEmail(email, "Kích hoạt tài khoản", body);

        return user;
    }

    public boolean activateUser(HttpServletRequest request) {
        String data = cookieService.getCookie(request, "activation");
        if (data == null) {
            return false;
        }

        String[] parts = data.split(":");
        int userId = Integer.parseInt(parts[0]);
        Optional<Account> userOpt = accountDAO.findById(userId);

        if (userOpt.isPresent()) {
            Account user = userOpt.get();
            user.setActivated(true);
            accountDAO.save(user);
            return true;
        }

        return false;
    }

    public boolean forgotPassword(String email, HttpServletResponse response) throws MessagingException {
        Optional<Account> userOpt = accountDAO.findByEmail(email);
        if (userOpt.isEmpty()) {
            return false;
        }
        // Tạo token ngẫu nhiên và lưu vào cookie
        String token = UUID.randomUUID().toString();
        cookieService.setCookie(response, "resetToken", token, 10 * 60); // 10 phút

        // Gửi email chứa liên kết đặt lại mật khẩu
        String resetLink = "http://localhost:8080/reset-password?token=" + token;
        String emailContent = "Nhấp vào liên kết sau để đặt lại mật khẩu: <a href='" + resetLink
                + "'>Đặt lại mật khẩu</a>";
        mailService.sendEmail(email, "Đặt lại mật khẩu", emailContent);

        return true;
    }

    public boolean resetPassword(HttpServletRequest request, String newPassword) {
        String data = cookieService.getCookie(request, "resetPassword");
        if (data == null) {
            return false;
        }

        String[] parts = data.split(":");
        int userId = Integer.parseInt(parts[0]);
        Optional<Account> userOpt = accountDAO.findById(userId);

        if (userOpt.isPresent()) {
            Account user = userOpt.get();
            user.setPassword(newPassword);
            accountDAO.save(user);
            return true;
        }

        return false;
    }
    public boolean isEmailExists(String email) {
        return accountDAO.findByEmail(email) != null;
    }

    // Kiểm tra username đã tồn tại chưa
    public boolean isUsernameExists(String username) {
        return accountDAO.findByUsername(username) != null;
    }

}
