package mk.ukim.finki.ib.authentication.service;

public interface SessionService {
    void storeSession(String sessionToken, String username);
    String getUsernameBySession(String sessionToken);
    void removeSession(String sessionToken);
    boolean isSessionValid(String sessionToken);
}
