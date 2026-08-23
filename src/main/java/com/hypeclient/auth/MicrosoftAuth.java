package com.hypeclient.auth;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Microsoft OAuth Authentication for Minecraft
 * Supports Microsoft Account login
 */
public class MicrosoftAuth {
    private static final Logger logger = LoggerFactory.getLogger(MicrosoftAuth.class);
    
    private static final String CLIENT_ID = "00000000402b5328"; // Minecraft Launcher Client ID
    private static final String REDIRECT_URI = "http://localhost:8080";
    private static final String SCOPES = "XboxLive.signin offline_access";
    
    private String accessToken;
    private String refreshToken;
    private String username;
    private String uuid;

    /**
     * Start Microsoft OAuth flow
     */
    public void startAuthFlow() {
        logger.info("Starting Microsoft OAuth flow...");
        
        String authUrl = String.format(
            "https://login.microsoftonline.com/consumers/oauth2/v2.0/authorize?" +
            "client_id=%s&redirect_uri=%s&response_type=code&scope=%s",
            CLIENT_ID, REDIRECT_URI, SCOPES
        );
        
        logger.info("Auth URL: " + authUrl);
        openBrowser(authUrl);
    }

    /**
     * Exchange authorization code for tokens
     */
    public boolean authenticate(String authCode) {
        try {
            HttpClient client = HttpClients.createDefault();
            HttpPost post = new HttpPost("https://login.microsoftonline.com/consumers/oauth2/v2.0/token");
            
            String body = String.format(
                "client_id=%s&redirect_uri=%s&code=%s&grant_type=authorization_code",
                CLIENT_ID, REDIRECT_URI, authCode
            );
            
            post.setEntity(new StringEntity(body));
            post.setHeader("Content-Type", "application/x-www-form-urlencoded");
            
            var response = client.execute(post, classicHttpResponse -> {
                BufferedReader reader = new BufferedReader(
                    new InputStreamReader(classicHttpResponse.getEntity().getContent())
                );
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                return sb.toString();
            });
            
            JsonObject json = JsonParser.parseString(response).getAsJsonObject();
            
            if (json.has("access_token")) {
                this.accessToken = json.get("access_token").getAsString();
                this.refreshToken = json.get("refresh_token").getAsString();
                
                logger.info("✅ Microsoft authentication successful!");
                return getMinecraftProfile();
            } else {
                logger.error("❌ Authentication failed: " + json.get("error").getAsString());
                return false;
            }
            
        } catch (Exception e) {
            logger.error("❌ Authentication error", e);
            return false;
        }
    }

    /**
     * Get Minecraft profile from Xbox Live
     */
    private boolean getMinecraftProfile() {
        try {
            HttpClient client = HttpClients.createDefault();
            HttpPost post = new HttpPost("https://api.minecraftservices.com/authentication/login_with_xbox");
            
            String body = "{\"identityToken\": \"" + accessToken + "\"}";
            post.setEntity(new StringEntity(body));
            post.setHeader("Content-Type", "application/json");
            
            var response = client.execute(post, classicHttpResponse -> {
                BufferedReader reader = new BufferedReader(
                    new InputStreamReader(classicHttpResponse.getEntity().getContent())
                );
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                return sb.toString();
            });
            
            JsonObject json = JsonParser.parseString(response).getAsJsonObject();
            this.username = json.get("name").getAsString();
            this.uuid = json.get("id").getAsString();
            
            logger.info("✅ Got Minecraft profile: " + this.username);
            return true;
            
        } catch (Exception e) {
            logger.error("❌ Failed to get Minecraft profile", e);
            return false;
        }
    }

    private void openBrowser(String url) {
        try {
            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
            } else if (System.getProperty("os.name").toLowerCase().contains("mac")) {
                Runtime.getRuntime().exec(new String[]{"open", url});
            } else {
                Runtime.getRuntime().exec(new String[]{"xdg-open", url});
            }
        } catch (Exception e) {
            logger.error("Failed to open browser", e);
        }
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getUsername() {
        return username;
    }

    public String getUUID() {
        return uuid;
    }
}
