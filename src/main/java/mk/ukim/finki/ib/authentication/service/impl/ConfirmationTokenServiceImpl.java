package mk.ukim.finki.ib.authentication.service.impl;

import lombok.AllArgsConstructor;
import mk.ukim.finki.ib.authentication.model.ConfirmationToken;
import mk.ukim.finki.ib.authentication.model.User;
import mk.ukim.finki.ib.authentication.repository.ConfirmationTokenRepository;
import mk.ukim.finki.ib.authentication.service.ConfirmationTokenService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ConfirmationTokenServiceImpl implements ConfirmationTokenService {
    private final ConfirmationTokenRepository confirmationTokenRepository;

    @Override
    public void saveConfirmationToken(ConfirmationToken token) {
        this.confirmationTokenRepository.save(token);
    }

    @Override
    public Optional<ConfirmationToken> getToken(String token) {
        return this.confirmationTokenRepository.findByToken(token);
    }

    @Override
    public void setConfirmedAt(String token) {
        this.confirmationTokenRepository.updateConfirmedAt(token, LocalDateTime.now());
    }

    @Override
    public String generateToken(User user) {
        String token = UUID.randomUUID().toString().substring(0,6);
        ConfirmationToken confirmationToken = new ConfirmationToken(token, user);
        this.confirmationTokenRepository.save(confirmationToken);
        return token;
    }

    @Override
    public List<ConfirmationToken> findByUser(User user) {
        return this.confirmationTokenRepository.findAllByUser(user);
    }
}
