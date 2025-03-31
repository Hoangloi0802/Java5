package ass.ass.DAO;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ass.ass.model.Account;
import ass.ass.model.Order;

public interface OrderDAO extends JpaRepository<Order, Long> {
    List<Order> findByAccount(Account account);
}