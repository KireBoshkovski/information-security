package mk.ukim.finki.ib.authentication.service;

import mk.ukim.finki.ib.authentication.model.ConfirmationToken;
import mk.ukim.finki.ib.authentication.model.User;

import java.util.List;
import java.util.Optional;

public interface ConfirmationTokenService {
    void saveConfirmationToken(ConfirmationToken token);

    Optional<ConfirmationToken> getToken(String token);

    void setConfirmedAt(String token);

    String generateToken(User user);

    List<ConfirmationToken> findByUser(User user);
}
