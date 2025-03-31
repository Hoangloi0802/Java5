package ass.ass.Auth;

import ass.ass.model.Account;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // Lấy session
        HttpSession session = request.getSession();
        Account user = (Account) session.getAttribute("user");

        // Các URL không yêu cầu đăng nhập 
        String uri = request.getRequestURI();
        if (uri.equals("/") || uri.startsWith("/products") ||
                uri.startsWith("/contact") || uri.startsWith("/login") || uri.startsWith("/dangky") ||
                uri.startsWith("/css") || uri.startsWith("/js") || uri.startsWith("/images")
                || uri.startsWith("/img")) {
            return true; 
        }

        if (user == null && (uri.startsWith("/profile") || uri.startsWith("/cart") || uri.startsWith("/orders"))) {
            response.sendRedirect("/login"); // Chuyển hướng về trang đăng nhập
            return false;
        }

        if (uri.startsWith("/admin") && (user == null || !user.isAdmin())) {
            response.sendRedirect("/"); // Chuyển hướng đến trang chủ
            return false;
        }

        return true; 
    }
}
