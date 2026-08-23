package com.hypeclient.discord;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * Discord Rich Presence integration
 * Shows "Playing HYPE CLIENT" on Discord status
 */
public class DiscordRPC {
    private static final Logger logger = LoggerFactory.getLogger(DiscordRPC.class);
    
    private static final long CLIENT_ID = 1234567890L; // Replace with your Discord app ID
    private static final String VERSION = "1.0";
    
    private Socket socket;
    private OutputStream out;
    private boolean connected = false;

    public void connect() {
        try {
            // Connect to Discord RPC
            socket = new Socket("127.0.0.1", 6463);
            out = socket.getOutputStream();
            
            // Handshake
            String handshake = String.format(
                "{\"v\": 1, \"client_id\": \"%d\"}",
                CLIENT_ID
            );
            
            sendData(0, handshake);
            connected = true;
            logger.info("✅ Connected to Discord RPC");
            
        } catch (Exception e) {
            logger.warn("⚠️ Discord RPC not available: " + e.getMessage());
            connected = false;
        }
    }

    public void updatePresence(String state, String details) {
        if (!connected) return;
        
        try {
            String presence = String.format(
                "{" +
                "\"cmd\": \"SET_ACTIVITY\"," +
                "\"args\": {" +
                "\"pid\": %d," +
                "\"activity\": {" +
                "\"state\": \"%s\"," +
                "\"details\": \"%s\"," +
                "\"assets\": {" +
                "\"large_image\": \"hype_client_logo\"," +
                "\"large_text\": \"HYPE CLIENT\"" +
                "}," +
                "\"timestamps\": {" +
                "\"start\": %d" +
                "}" +
                "}" +
                "}" +
                "}",
                ProcessHandle.current().pid(),
                state,
                details,
                System.currentTimeMillis() / 1000
            );
            
            sendData(1, presence);
            logger.info("📊 Discord presence updated: " + state + " - " + details);
            
        } catch (Exception e) {
            logger.error("Error updating Discord presence", e);
        }
    }

    public void updateGameStatus(String username, String server, String cosmetics) {
        if (!connected) return;
        
        String details = String.format("Playing on %s", server);
        String state = String.format("As %s | %s", username, cosmetics);
        updatePresence(state, details);
    }

    private void sendData(int opcode, String data) throws IOException {
        byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
        
        // Write opcode (4 bytes, little-endian)
        out.write(opcode & 0xFF);
        out.write((opcode >> 8) & 0xFF);
        out.write((opcode >> 16) & 0xFF);
        out.write((opcode >> 24) & 0xFF);
        
        // Write length (4 bytes, little-endian)
        int length = dataBytes.length;
        out.write(length & 0xFF);
        out.write((length >> 8) & 0xFF);
        out.write((length >> 16) & 0xFF);
        out.write((length >> 24) & 0xFF);
        
        // Write data
        out.write(dataBytes);
        out.flush();
    }

    public void disconnect() {
        try {
            if (socket != null) {
                socket.close();
            }
            connected = false;
            logger.info("Disconnected from Discord RPC");
        } catch (IOException e) {
            logger.error("Error disconnecting from Discord RPC", e);
        }
    }

    public boolean isConnected() {
        return connected;
    }
}
