package mk.ukim.finki.ib.authentication.service;

public interface SessionService {
    void storeSession(String sessionToken, String username);
    void removeSession(String sessionToken);
    boolean isSessionValid(String sessionToken);
    String checkAuthority(String sessionToken);
}
