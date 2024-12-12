package mk.ukim.finki.ib.authentication.service.impl;

import lombok.AllArgsConstructor;
import mk.ukim.finki.ib.authentication.model.User;
import mk.ukim.finki.ib.authentication.model.exceptions.InvalidArgumentsException;
import mk.ukim.finki.ib.authentication.model.exceptions.UserAlreadyExistsException;
import mk.ukim.finki.ib.authentication.repository.UserRepository;
import mk.ukim.finki.ib.authentication.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public void validate(String username, String email, String password, String repeatedPassword) {
        if (username == null || email == null || password == null || username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            throw new InvalidArgumentsException("Please fill all required fields");
        }
        if (!password.equals(repeatedPassword)) {
            throw new InvalidArgumentsException("Passwords do not match");
        }
            if (this.userRepository.findByUsername(username).isPresent() && this.userRepository.findByUsername(username).get().isConfirmed()) {
                throw new UserAlreadyExistsException("username " + username);
            }
            if (this.userRepository.findByEmail(email).isPresent() && this.userRepository.findByEmail(email).get().isConfirmed()) {
                throw new UserAlreadyExistsException("email " + email);
            }
    }

    @Override
    public void save(User user) {
        this.userRepository.save(user);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }

    @Override
    public boolean isConfirmed(String username) {
        return this.userRepository.findByUsername(username).get().isConfirmed();
    }

    @Override
    public Optional<User> findByUsername(String username) {

        return this.userRepository.findByUsername(username);
    }


}
