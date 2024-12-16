package mk.ukim.finki.ib.authentication.service.impl;

import mk.ukim.finki.ib.authentication.model.exceptions.UserNotFoundException;
import mk.ukim.finki.ib.authentication.repository.UserRepository;
import mk.ukim.finki.ib.authentication.service.SessionService;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class SessionServiceImpl implements SessionService {
    private final ConcurrentHashMap<String, String> sessions = new ConcurrentHashMap<>();
    private final UserRepository userRepository;

    public SessionServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void storeSession(String sessionToken, String username) {
        this.sessions.put(sessionToken, username);
    }

    @Override
    public void removeSession(String sessionToken) {
        this.sessions.remove(sessionToken);
    }

    @Override
    public boolean isSessionValid(String sessionToken) {
        return this.sessions.containsKey(sessionToken);
    }

    @Override
    public String checkAuthority(String sessionToken) {
        String username = this.sessions.get(sessionToken);

        if (username == null) {
            return null;
        }

        return this.userRepository.findByUsername(username).get().getRole().toString();
    }
}
