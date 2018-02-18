package mq.coin.autobuyer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Component
public class AuthRequestInterceptor implements ClientHttpRequestInterceptor {

    @Autowired
    private AuthUtils authUtils;

    @Override
    public ClientHttpResponse intercept(HttpRequest httpRequest, byte[] body, ClientHttpRequestExecution clientHttpRequestExecution) throws IOException {
        try {
            authUtils.setAuthHeaders(httpRequest.getHeaders(), httpRequest.getMethod().name(), httpRequest.getURI().toString(), bodyToString(httpRequest, body));
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
        return clientHttpRequestExecution.execute(httpRequest, body);
    }

    private String getPath(URI uri){
        if (StringUtils.isEmpty(uri.getQuery())){
            return uri.getPath();
        }
        return String.format("%s?%s", uri.getPath(), uri.getQuery());
    }

    private String bodyToString(HttpRequest request, byte[] body) {
        StringBuilder builder = new StringBuilder();
        if (body.length > 0 && hasTextBody(request.getHeaders())) {
            builder.append(new String(body, determineCharset(request.getHeaders())));
        }
        return builder.toString();
    }

    private boolean hasTextBody(HttpHeaders headers) {
        MediaType contentType = headers.getContentType();
        if (contentType != null) {
            String subtype = contentType.getSubtype();
            return "text".equals(contentType.getType()) || "xml".equals(subtype) || "json".equals(subtype);
        }
        return false;
    }

    private Charset determineCharset(HttpHeaders headers) {
        MediaType contentType = headers.getContentType();
        if (contentType != null) {
            try {
                Charset charSet = contentType.getCharSet();
                if (charSet != null) {
                    return charSet;
                }
            } catch (UnsupportedCharsetException e) {
                // ignore
            }
        }
        return StandardCharsets.UTF_8;
    }
}
