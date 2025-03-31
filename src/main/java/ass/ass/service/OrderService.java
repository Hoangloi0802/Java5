package ass.ass.service;

import ass.ass.DAO.OrderDAO;
import ass.ass.DAO.AccountDAO;
import ass.ass.DAO.OrderDetailDAO;
import ass.ass.DAO.ProductDAO;
import ass.ass.model.Order;
import ass.ass.model.Account;
import ass.ass.model.OrderDetail;
import ass.ass.model.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {
    private final OrderDAO orderDAO;
    private final AccountDAO accountDAO;
    private final OrderDetailDAO orderDetailDAO;
    private final ProductDAO productDAO;

    public OrderService(OrderDAO orderDAO, AccountDAO accountDAO, OrderDetailDAO orderDetailDAO,
            ProductDAO productDAO) {
        this.orderDAO = orderDAO;
        this.accountDAO = accountDAO;
        this.orderDetailDAO = orderDetailDAO;
        this.productDAO = productDAO;
    }

    //  Tạo đơn hàng từ giỏ hàng
    @Transactional
    public Order createOrder(String username, Map<Long, Integer> cartItems) {
        Account account = accountDAO.findByUsername(username);

        if (cartItems.isEmpty()) {
            throw new RuntimeException("Lỗi: Giỏ hàng trống, không thể đặt hàng!");
        }

        Order order = new Order();
        order.setAccount(account);
        order.setCreateDate(new Date());
        orderDAO.save(order);

        for (Map.Entry<Long, Integer> entry : cartItems.entrySet()) {
            Long productId = entry.getKey();
            Integer quantity = entry.getValue();

            Product product = productDAO.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Lỗi: Không tìm thấy sản phẩm có ID: " + productId));

            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            orderDetail.setProduct(product);
            orderDetail.setQuantity(quantity);
            orderDetail.setPrice(product.getPrice() * quantity);
            orderDetailDAO.save(orderDetail);
        }

        return order;
    }

    //  Lấy danh sách tất cả đơn hàng 
    public List<Order> getAllOrders() {
        return orderDAO.findAll();
    }

    //  Lấy đơn hàng theo ID
    public Order getOrderById(Long id) {
        return orderDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Lỗi: Không tìm thấy đơn hàng!"));
    }

    //  Lấy danh sách đơn hàng của một người dùng
    public List<Order> getOrdersByUser(String username) {
        Account account = accountDAO.findByUsername(username);

        return orderDAO.findByAccount(account);
    }

}
