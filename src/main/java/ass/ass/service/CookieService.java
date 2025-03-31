package ass.ass.service;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import java.util.Base64;

@Service
public class CookieService {

    public void setCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, Base64.getEncoder().encodeToString(value.getBytes()));
        cookie.setMaxAge(maxAge);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public String getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return new String(Base64.getDecoder().decode(cookie.getValue()));
                }
            }
        }
        return null;
    }

    public void deleteCookie(HttpServletResponse response, String name) {
        setCookie(response, name, "", 0);
    }
}
