package mk.ukim.finki.ib.authentication.service;

import mk.ukim.finki.ib.authentication.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    void validate(String username, String email, String password, String repeatedPassword);

    void save(User user);

    Optional<User> findByEmail(String email);

    boolean isConfirmed(String username);

    Optional<User> findByUsername(String username);

    List<User> findAll();

    void deleteByUsername(String username);
}
