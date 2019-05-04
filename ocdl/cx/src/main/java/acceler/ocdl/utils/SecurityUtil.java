package acceler.ocdl.utils;

import acceler.ocdl.model.InnerUser;
import org.springframework.stereotype.Component;

import java.util.Hashtable;
import java.util.Map;
import java.util.Random;

@Component
public class SecurityUtil {
    private static final String TOKEN_CHARS = "abcdefghijklmnopqrstuvwsyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
    private static final int LENGTH_TOKEN = 32;

    private final Map<String, InnerUser> inMemoryTokenManager = new Hashtable<>();

    public String requestToken(InnerUser innerUser) {
        for (Map.Entry entry : inMemoryTokenManager.entrySet()) {
            if (((InnerUser) entry.getValue()).getUserId().equals(innerUser.getUserId())) {
                return (String) entry.getKey();
            }
        }
        //innerUser never get token
        String token = generateToken();
        this.inMemoryTokenManager.put(token, innerUser);
        return token;
    }

    public boolean isUserLogin(InnerUser innerUser) {
        return inMemoryTokenManager.values().contains(innerUser);
    }

    public InnerUser getUserByToken(String token){
        return this.inMemoryTokenManager.get(token);
    }

    public void releaseToken(InnerUser innerUser){
        String token = null;
        for (Map.Entry<String, InnerUser> entry : this.inMemoryTokenManager.entrySet()) {
            if (innerUser.getUserId().equals(entry.getValue().getUserId())) {
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
