package com.hypeclient.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

/**
 * Offline Mode Authentication
 * Allows playing without Microsoft account
 */
public class OfflineAuth {
    private static final Logger logger = LoggerFactory.getLogger(OfflineAuth.class);
    
    private String username;
    private String uuid;

    /**
     * Create offline account with custom username
     */
    public void authenticate(String username) {
        this.username = username;
        // Generate deterministic UUID based on username
        this.uuid = UUID.nameUUIDFromBytes(("OfflinePlayer:" + username).getBytes()).toString();
        
        logger.info("✅ Offline authentication successful!");
        logger.info("   Username: " + this.username);
        logger.info("   UUID: " + this.uuid);
    }

    public String getUsername() {
        return username;
    }

    public String getUUID() {
        return uuid;
    }

    public boolean isValid() {
        return username != null && !username.isEmpty();
    }
}
