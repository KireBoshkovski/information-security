package mk.ukim.finki.ib.authentication.service.impl;

import mk.ukim.finki.ib.authentication.service.AuthService;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AuthServiceImpl implements AuthService {

    @Override
    public String generate2FACode() {
        // Generate a secure 6-digit code
        SecureRandom random = new SecureRandom();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }
}
