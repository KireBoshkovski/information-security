package mk.ukim.finki.ib.authentication.web.controller;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import mk.ukim.finki.ib.authentication.model.ConfirmationToken;
import mk.ukim.finki.ib.authentication.model.User;
import mk.ukim.finki.ib.authentication.security.PasswordEncoder;
import mk.ukim.finki.ib.authentication.service.ConfirmationTokenService;
import mk.ukim.finki.ib.authentication.service.EmailService;
import mk.ukim.finki.ib.authentication.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/register")
@AllArgsConstructor
public class RegisterController {
    private final UserService userService;
    private final ConfirmationTokenService tokenService;
    private final EmailService emailService;

    @GetMapping
    public String getRegisterPage(@RequestParam(required = false) String error, Model model) {
        if (error != null && !error.isEmpty()) {
            model.addAttribute("hasError", true);
            model.addAttribute("error", error);
        }

        return "register";
    }

    @PostMapping
    public String validate(@RequestParam String username, @RequestParam String email, @RequestParam String password,
                           @RequestParam String repeatedPassword) {
        try {
            this.userService.validate(username, email, password, repeatedPassword);
            return "forward:/register/sendConfirmationEmail";
        } catch (RuntimeException ex) {
            return "redirect:/register?error=" + ex.getMessage();
        }
    }

    @PostMapping("/sendConfirmationEmail")
    public String sendConfirmationEmail(@RequestParam String username, @RequestParam String email, @RequestParam String password, HttpSession session) {
        session.setAttribute("email", email);

        String encodedPassword = PasswordEncoder.hashPassword(password);

        User tempUser = new User(username, email, encodedPassword);
        session.setAttribute("user", tempUser);
        this.userService.save(tempUser);

        String token = this.tokenService.generateToken(tempUser);
        this.emailService.sendConfirmationCode(email, token);

        return "redirect:/register/verify";
    }

    @GetMapping("/verify")
    public String getVerificationPage(@RequestParam(required = false) String error, Model model) {
        if (error != null && !error.isEmpty()) {
            model.addAttribute("hasError", true);
            model.addAttribute("error", error);
        }
        return "verify-user";
    }

    @PostMapping("/verify")
    public String verifyCode(@RequestParam String code, HttpSession session) {
        User user = (User) session.getAttribute("user");

        List<ConfirmationToken> tokens = this.tokenService.findByUser(user);
        ConfirmationToken token;
        token = tokens.stream().filter(t -> t.getToken().equals(code)).findFirst().orElse(null);

        if (token != null) {
            user.setConfirmed(true);
            this.userService.save(user);
            token.setConfirmedAt(LocalDateTime.now());
            this.tokenService.saveConfirmationToken(token);
            System.out.println("Email successfully verified");
            return "redirect:/login";
        } else {
            System.out.println("No tokens found for the specified user.");
            return "redirect:/register/verify?error=No tokens found for the specified user.";
        }
    }
}
