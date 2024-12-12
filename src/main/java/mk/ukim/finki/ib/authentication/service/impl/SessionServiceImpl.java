package mk.ukim.finki.ib.authentication.service.impl;

import mk.ukim.finki.ib.authentication.service.SessionService;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class SessionServiceImpl implements SessionService {
    private final ConcurrentHashMap<String, String> sessions = new ConcurrentHashMap<>();

    @Override
    public void storeSession(String sessionToken, String username) {
        this.sessions.put(sessionToken, username);
    }

    @Override
    public String getUsernameBySession(String sessionToken) {
        return this.sessions.get(sessionToken);
    }

    @Override
    public void removeSession(String sessionToken) {
        this.sessions.remove(sessionToken);
    }

    @Override
    public boolean isSessionValid(String sessionToken) {
        return this.sessions.containsKey(sessionToken);
    }
}
