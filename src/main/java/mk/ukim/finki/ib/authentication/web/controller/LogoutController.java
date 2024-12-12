package mk.ukim.finki.ib.authentication.web.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import mk.ukim.finki.ib.authentication.service.SessionService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/logout")
@AllArgsConstructor
public class LogoutController {
    private final SessionService sessionService;

    @PostMapping("/logout")
    public String logout(@CookieValue(name = "SESSIONID", required = false) String sessionToken, HttpServletResponse response) {
        if (sessionToken != null) {
            this.sessionService.removeSession(sessionToken);

            // Remove the cookie
            Cookie cookie = new Cookie("SESSIONID", null);
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            cookie.setPath("/");
            cookie.setMaxAge(0); // Expire the cookie
            response.addCookie(cookie);
        }

        return "redirect:/login";
    }
}
