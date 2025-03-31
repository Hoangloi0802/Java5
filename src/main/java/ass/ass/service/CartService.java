package ass.ass.service;

import ass.ass.DAO.ProductDAO;
import ass.ass.model.Product;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CartService {
    private final ProductDAO productDAO;
    private final Map<Long, Integer> cart = new HashMap<>(); // Lưu ID sản phẩm & số lượng

    public CartService(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    public List<Product> getCartItems() {
        List<Product> cartItems = new ArrayList<>();
        for (Map.Entry<Long, Integer> entry : cart.entrySet()) {
            Product product = productDAO.findById(entry.getKey()).orElse(null);
            if (product != null) {
                cartItems.add(product);
            }
        }
        return cartItems;
    }

    public Map<Long, Integer> getCartQuantities() {
        return cart;
    }

    public double getTotalPrice() {
        double total = 0;
        for (Map.Entry<Long, Integer> entry : cart.entrySet()) {
            Product product = productDAO.findById(entry.getKey()).orElse(null);
            if (product != null) {
                total += product.getPrice() * entry.getValue();
            }
        }
        return total;
    }

    public void addToCart(Long id) {
        cart.put(id, cart.getOrDefault(id, 0) + 1);
    }

    public void updateCart(Long id, int quantity) {
        if (quantity > 0) {
            cart.put(id, quantity);
        } else {
            cart.remove(id);
        }
    }

    public void removeFromCart(Long id) {
        cart.remove(id);
    }

    public void clearCart() {
        cart.clear();
    }
    
}
