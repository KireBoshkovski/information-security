package mk.ukim.finki.ib.authentication.service;

public interface EmailService {
    void sendConfirmationCode(String to, String email);

    void send2FACode(String email, String code);
}
