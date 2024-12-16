package mk.ukim.finki.ib.authentication.web.controller;

import lombok.AllArgsConstructor;
import mk.ukim.finki.ib.authentication.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminController {
    private final UserService userService;

    @GetMapping
    public String getAdminPage(Model model) {
        model.addAttribute("users", this.userService.findAll());
        return "user-list";
    }

    @PostMapping
    public String removeUser(@RequestParam String username) {
        this.userService.deleteByUsername(username);
        return "redirect:/admin";
    }
}
