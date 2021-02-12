package acceler.ocdl.utils;

import acceler.ocdl.entity.User;
import org.springframework.stereotype.Component;

import java.util.Hashtable;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Component
public class SecurityUtil {
    private static final String TOKEN_CHARS = "abcdefghijklmnopqrstuvwsyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
    private static final int LENGTH_TOKEN = 32;

    private final Map<String, SecurityUser> inMemoryTokenManager = new Hashtable<>();
    private final long intervalThreshold = 1L; //1 hour
    private static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public String requestToken(User user) {

        for (Map.Entry entry : inMemoryTokenManager.entrySet()) {
            if (((SecurityUser) entry.getValue()).getUser().getId().equals(user.getId())) {
                return (String) entry.getKey();
            }
        }

        //innerUser never get token
        String token = generateToken();
        this.inMemoryTokenManager.put(token, new SecurityUser(TimeUtil.currentTime(), user));
        return token;
    }

    public boolean isUserLogin(User user) {

        for (SecurityUser securityUser : this.inMemoryTokenManager.values()) {
            if (securityUser.getUser().getId().equals(user.getId())) {
                return true;
            }
        }
        return false;
    }

    public User getUserByToken(String token){
        if (this.inMemoryTokenManager.containsKey(token)) {
            long diffInterval = (TimeUtil.currentTime().getTime() - this.inMemoryTokenManager.get(token).getRequestTime().getTime()) / (1000 * 60 * 60);
            if (diffInterval <= intervalThreshold) {
                this.inMemoryTokenManager.get(token).setRequestTime(TimeUtil.currentTime());
                return this.inMemoryTokenManager.get(token).getUser();
            } else {
                this.inMemoryTokenManager.remove(token);
                return null;
            }
        } else {
            return null;
        }
    }

    public void releaseToken(User user){
        String token = null;
        for (Map.Entry<String, SecurityUser> entry : this.inMemoryTokenManager.entrySet()) {
            if (user.getId().equals(entry.getValue().getUser().getId())) {
                token = entry.getKey();
            }
        }
        if(token != null){
            this.inMemoryTokenManager.remove(token);
        }
    }

    private String generateToken() {
        StringBuilder token = new StringBuilder();
        Random r = new Random();
        while (token.length() < LENGTH_TOKEN) {
            int index = (int) (r.nextFloat() * TOKEN_CHARS.length());
            token.append(TOKEN_CHARS.charAt(index));
        }
        return token.toString();
    }
}
