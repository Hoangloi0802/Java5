package ass.ass.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ass.ass.DAO.OrderDAO;
import ass.ass.DAO.OrderDetailDAO;
import ass.ass.DAO.AccountDAO;
import ass.ass.model.Order;
import ass.ass.model.Account;

@Service
public class DonhangService {

    @Autowired
    private OrderDAO orderRepository;

    @Autowired
    private AccountDAO accountRepository;
    @Autowired
    private OrderDetailDAO orderDetailRepository;

    // Lấy danh sách tất cả đơn hàng
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // Lấy đơn hàng theo ID
    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    // Lưu đơn hàng (tạo mới hoặc cập nhật)
    @Transactional
    public void saveOrder(Order order) {
        if (order.getAccount() != null && order.getAccount().getUsername() != null) {
            Optional<Account> existingAccount = accountRepository.findById(order.getAccount().getId());
            if (existingAccount.isPresent()) {
                order.setAccount(existingAccount.get());
            } else {
                throw new RuntimeException("Account không tồn tại!");
            }
        }
        orderRepository.save(order);
    }

    // Cập nhật đơn hàng
    @Transactional
    public void updateOrder(Order order) {
        if (order.getId() == null || !orderRepository.existsById(order.getId())) {
            throw new RuntimeException("Order không tồn tại!");
        }
        saveOrder(order);
    }

    // Xóa đơn hàng theo ID
    @Transactional
    public void deleteOrder(Long id) {
        Order order = orderRepository.findById(id).orElse(null);
            orderDetailRepository.deleteByOrder(order);
            orderRepository.delete(order);
       
    }

}
