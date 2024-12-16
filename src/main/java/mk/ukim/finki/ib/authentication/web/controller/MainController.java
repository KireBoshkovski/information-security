package mk.ukim.finki.ib.authentication.web.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import mk.ukim.finki.ib.authentication.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping({"/", ""})
@AllArgsConstructor
public class MainController {
    @GetMapping("/home")
    public String getHomePage(HttpServletRequest request, Model model) {
        User user = (User) request.getSession().getAttribute("user");

        model.addAttribute("username", user.getUsername());

        return "home";
    }

    @GetMapping("/unauthorized")
    public String getErrorPage() {
        return "error-page";
    }
}
