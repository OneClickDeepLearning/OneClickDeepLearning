package acceler.ocdl.utils;

import acceler.ocdl.exception.NotFoundException;
import acceler.ocdl.model.InnerUser;
import org.springframework.stereotype.Component;

import java.util.Hashtable;
import java.util.Map;
import java.util.Random;

@Component
public class SecurityUtil {
    private static final String TOKEN_CHARS = "abcdefghijklmnopqrstuvwsyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
    private static final int LENGTH_TOKEN = 32;

    private final Map<String, SecurityUser> inMemoryTokenManager = new Hashtable<>();
    private final long intervalThreshold = 1L; //1 hour

    public String requestToken(InnerUser innerUser) {

        for (Map.Entry entry : inMemoryTokenManager.entrySet()) {
            if (((SecurityUser) entry.getValue()).getInnerUser().getUserId().equals(innerUser.getUserId())) {
                return (String) entry.getKey();
            }
        }

        //innerUser never get token
        String token = generateToken();
        this.inMemoryTokenManager.put(token, new SecurityUser(TimeUtil.currentTime(), innerUser));
        return token;
    }

    public boolean isUserLogin(InnerUser innerUser) {

        for (SecurityUser securityUser : this.inMemoryTokenManager.values()) {
            if (securityUser.getInnerUser().getUserId().equals(innerUser.getUserId())) {
                return true;
            }
        }
        return false;
    }

    public InnerUser getUserByToken(String token){
        if (this.inMemoryTokenManager.containsKey(token)) {
            long diffInterval = (TimeUtil.currentTime().getTime() - this.inMemoryTokenManager.get(token).getRequestTime().getTime()) / (1000 * 60 * 60);
            if (diffInterval <= intervalThreshold) {
                this.inMemoryTokenManager.get(token).setRequestTime(TimeUtil.currentTime());
                return this.inMemoryTokenManager.get(token).getInnerUser();
            } else {
                this.inMemoryTokenManager.remove(token);
                throw new NotFoundException("Fail to find the token, your token maybe expired");
            }
        } else {
            throw new NotFoundException("Fail to find the token, your token maybe expired");
        }
    }

    public void releaseToken(InnerUser innerUser){
        String token = null;
        for (Map.Entry<String, SecurityUser> entry : this.inMemoryTokenManager.entrySet()) {
            if (innerUser.getUserId().equals(entry.getValue().getInnerUser().getUserId())) {
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
