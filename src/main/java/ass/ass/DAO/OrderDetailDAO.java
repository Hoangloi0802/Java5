package ass.ass.DAO;

import org.springframework.data.jpa.repository.JpaRepository;

import ass.ass.model.Order;
import ass.ass.model.OrderDetail;

public interface OrderDetailDAO extends JpaRepository<OrderDetail, Long>{
    void deleteByOrder(Order order);
}