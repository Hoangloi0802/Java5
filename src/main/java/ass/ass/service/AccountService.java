package ass.ass.service;

import ass.ass.DAO.AccountDAO;
import ass.ass.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService {
    @Autowired
    private AccountDAO accountDAO;

    private static final String IMAGE_DIR = "src/main/resources/static/images/";

    public List<Account> getAllAccounts() {
        return accountDAO.findAll();
    }

    public Optional<Account> getAccountById(Integer id) {
        return accountDAO.findById(id);
    }
    public void updateAccount(Account account, MultipartFile file) throws IOException {
        Account existingAccount = accountDAO.findById(account.getId()).orElse(null);
        if (existingAccount != null) {
    
            // Cập nhật ảnh đại diện nếu có
            if (!file.isEmpty()) {
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                Path uploadPath = Paths.get(IMAGE_DIR);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                Path filePath = uploadPath.resolve(fileName);
                Files.write(filePath, file.getBytes());
                existingAccount.setPhoto(fileName);
            }
    
            // Nếu mật khẩu mới không nhập, giữ mật khẩu cũ
            if (account.getPassword() == null || account.getPassword().isEmpty()) {
                account.setPassword(existingAccount.getPassword());
            }
            
            // Cập nhật thông tin khác
            existingAccount.setPassword(account.getPassword());
            existingAccount.setFullname(account.getFullname());
            existingAccount.setEmail(account.getEmail());
            existingAccount.setActivated(account.isActivated());
            existingAccount.setAdmin(account.isAdmin());
    
            // Lưu lại vào database
            accountDAO.save(existingAccount);
        }
    }
    
    public void deleteAccount(Integer id) {
        accountDAO.deleteById(id);
    }

    
}
