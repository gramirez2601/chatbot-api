package com.xumtech.chatbot.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * A servlet filter that validates incoming requests using an HMAC signature.
 */
public class SecurityFilter implements Filter {

    private final String apiKey;
    private final String apiSecret;

    public SecurityFilter(String apiKey, String apiSecret) {
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        if ("OPTIONS".equalsIgnoreCase(httpRequest.getMethod())) {
            httpResponse.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        // 1. Read all required headers from the BFF request
        String requestApiKey = httpRequest.getHeader("X-Api-Key");
        String requestTimestamp = httpRequest.getHeader("X-Timestamp");
        String requestSignature = httpRequest.getHeader("X-Signature");

        // 2. Validate that all headers are present
        if (requestApiKey == null || requestTimestamp == null || requestSignature == null) {
            sendUnauthorizedResponse(httpResponse, "Missing required security headers");
            return;
        }

        // 3. Validate the public API Key
        if (!this.apiKey.equals(requestApiKey)) {
            sendUnauthorizedResponse(httpResponse, "Invalid API Key");
            return;
        }

        // 4. Validate timestamp to prevent replay attacks (e.g., within 60 seconds)
        try {
            long requestTime = Long.parseLong(requestTimestamp);
            long currentTime = System.currentTimeMillis();
            if (Math.abs(currentTime - requestTime) > 60000) { // 60 seconds tolerance
                sendUnauthorizedResponse(httpResponse, "Request timestamp is too old");
                return;
            }
        } catch (NumberFormatException e) {
            sendUnauthorizedResponse(httpResponse, "Invalid timestamp format");
            return;
        }

        // 5. Calculate and validate the HMAC signature
        try {
            String expectedSignature = calculateHmacSha256(this.apiSecret, requestTimestamp);
            if (!MessageDigest.isEqual(expectedSignature.getBytes(StandardCharsets.UTF_8), requestSignature.getBytes(StandardCharsets.UTF_8))) {
                sendUnauthorizedResponse(httpResponse, "Invalid signature");
                return;
            }
        } catch (Exception e) {
            sendUnauthorizedResponse(httpResponse, "Error during signature validation");
            return;
        }

        // If all checks pass, proceed with the request
        chain.doFilter(request, response);
    }

    /**
     * Calculates the HMAC-SHA256 signature for a given message and secret.
     */
    private String calculateHmacSha256(String secret, String message) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac sha256Hmac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        sha256Hmac.init(secretKey);
        byte[] hmacBytes = sha256Hmac.doFinal(message.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hmacBytes);
    }

    /**
     * Helper method to send a standardized 401 Unauthorized response.
     */
    private void sendUnauthorizedResponse(HttpServletResponse httpResponse, String message) throws IOException {
        System.err.println("Authorization Failed: " + message);
        httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        httpResponse.getWriter().write("{\"error\":\"Unauthorized - " + message + "\"}");
        httpResponse.setContentType("application/json");
    }
}