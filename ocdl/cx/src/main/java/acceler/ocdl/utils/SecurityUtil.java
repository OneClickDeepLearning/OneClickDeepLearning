package acceler.ocdl.utils;

import acceler.ocdl.model.User;
import org.springframework.stereotype.Component;

import java.util.Hashtable;
import java.util.Map;
import java.util.Random;

@Component
public class SecurityUtil {
    private static final String TOKEN_CHARS = "abcdefghijklmnopqrstuvwsyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
    private static final int LENGTH_TOKEN = 32;

    private final Map<String, User> inMemoryTokenManager = new Hashtable<>();

    public String requestToken(User user) {
        for (Map.Entry entry : inMemoryTokenManager.entrySet()) {
            if (((User) entry.getValue()).getUserId().equals(user.getUserId())) {
                return (String) entry.getKey();
            }
        }
        //user never get token
        String token = generateToken();
        this.inMemoryTokenManager.put(token, user);
        return token;
    }

    public boolean isUserLogin(User user) {
        return inMemoryTokenManager.values().contains(user);
    }

    public User getUserByToken(String token){
        return this.inMemoryTokenManager.get(token);
    }

    public void releaseToken(User user){
        String token = null;
        for (Map.Entry<String, User> entry : this.inMemoryTokenManager.entrySet()) {
            if (user.getUserId().equals(entry.getValue().getUserId())) {
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
