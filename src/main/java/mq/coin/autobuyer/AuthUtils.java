package mq.coin.autobuyer;

import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Component
public class AuthUtils {

    @Autowired
    private AppProperties properties;

    @Value("${app.coin.api.key}")
    private String apiKey;

    @Value("${app.coin.api.secret}")
    private String apiSecret;

    public void setAuthHeaders(HttpHeaders httpHeaders, String method, String requestPath, String body) throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
        String timestamp = String.valueOf(System.currentTimeMillis());

        httpHeaders.add("CB-ACCESS-KEY", apiKey);
        httpHeaders.add("CB-ACCESS-SIGN", generateSignature(timestamp, method, requestPath, body));
        httpHeaders.add("CB-ACCESS-TIMESTAMP", timestamp);
    }

    private String generateSignature(String timestamp, String method, String requestPath, String body) throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
        String data = String.format("%s%s%s%s", timestamp, method, requestPath, body);
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(apiSecret.getBytes(), "HmacSHA256"));
        return new String(Hex.encodeHex(mac.doFinal(data.getBytes())));
    }
}
