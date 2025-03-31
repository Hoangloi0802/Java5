package ass.ass.DAO;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ass.ass.model.Account;

public interface AccountDAO extends JpaRepository<Account, Integer>{
    Account findByUsername(String username);
    Optional<Account> findByEmail(String email); 
}