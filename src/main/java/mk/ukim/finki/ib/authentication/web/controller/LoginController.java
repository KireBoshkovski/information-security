package mk.ukim.finki.ib.authentication.web.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import mk.ukim.finki.ib.authentication.model.User;
import mk.ukim.finki.ib.authentication.model.exceptions.UserNotFoundException;
import mk.ukim.finki.ib.authentication.security.PasswordEncoder;
import mk.ukim.finki.ib.authentication.service.AuthService;
import mk.ukim.finki.ib.authentication.service.EmailService;
import mk.ukim.finki.ib.authentication.service.SessionService;
import mk.ukim.finki.ib.authentication.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Controller
@RequestMapping("/login")
@AllArgsConstructor
public class LoginController {

    private final UserService userService;
    private final SessionService sessionService;
    private final AuthService authService;
    private final EmailService emailService;

    @GetMapping
    public String getLoginPage(@RequestParam(required = false) String error, Model model) {
         if (error != null && !error.isEmpty()) {
            model.addAttribute("hasError", true);
            model.addAttribute("error", error);
        }
        return "login";
    }

    @PostMapping
    public String verifyLogin(@RequestParam String username, @RequestParam String password, Model model, HttpServletRequest request) {
        request.getSession().setAttribute("username", username);

        if (this.userService.findByUsername(username).isPresent() && this.userService.isConfirmed(username)) {
            User user = this.userService.findByUsername(username).get();
            request.getSession().setAttribute("user", user);

            if (!PasswordEncoder.verifyPassword(password, user.getPassword())) {
                model.addAttribute("hasError", true);
                model.addAttribute("error", "Password is incorrect!");
                return "redirect:/login";
            } else { // continue
                return "redirect:/login/authenticate";
            }
        } else {
            model.addAttribute("hasError", true);
            model.addAttribute("error", "Username does not exist!");
            return "redirect:/login";
        }
    }

    @GetMapping("/authenticate")
    public String getAuthenticatePage(HttpServletRequest request) {
        String code = this.authService.generate2FACode();
        request.getSession().setAttribute("code", code);

        String username = request.getSession().getAttribute("username").toString();
        User user = this.userService.findByUsername(username).orElseThrow(() -> new UserNotFoundException("username " + username));

        // send authentication code
        this.emailService.send2FACode(user.getEmail(), code);

        return "authenticate-login";
    }

    @PostMapping("/authenticate")
    public String authenticateUser(@RequestParam String code, HttpServletRequest request, HttpServletResponse response, Model model) {
        String username = request.getSession().getAttribute("username").toString();
        User user = this.userService.findByUsername(username).orElseThrow(() -> new UserNotFoundException("username " + username));

        if (request.getSession().getAttribute("code").equals(code)) { // authenticated successfully

            String sessionToken = UUID.randomUUID().toString();

            Cookie cookie = new Cookie("SESSIONID", sessionToken);
            cookie.setHttpOnly(true); // Prevent JavaScript access for security
            cookie.setSecure(true); // Ensure the cookie is only sent over HTTPS
            cookie.setPath("/"); // Cookie applies to the entire application
            cookie.setMaxAge(3600); // Cookie expires in 1 hour
            response.addCookie(cookie);

            this.sessionService.storeSession(sessionToken, username);
            request.getSession().setAttribute("user", user);

            model.addAttribute("username", username);

            return "redirect:/home";
        } else {
            model.addAttribute("hasError", true);
            model.addAttribute("error", "Invalid code!");
            return "redirect:/login";
        }

    }

}
