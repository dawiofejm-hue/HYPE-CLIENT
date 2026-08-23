package com.hypeclient;

import com.hypeclient.ui.LauncherUI;
import com.hypeclient.discord.DiscordRPC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;

/**
 * HYPE CLIENT - Advanced Minecraft Launcher
 * Features: Microsoft OAuth, Offline Mode, Cosmetics, Discord RPC
 */
public class HypeClientLauncher {
    private static final Logger logger = LoggerFactory.getLogger(HypeClientLauncher.class);
    private static DiscordRPC discordRPC;

    public static void main(String[] args) {
        logger.info("🚀 Starting HYPE CLIENT v1.0.0...");
        
        try {
            // Initialize Discord RPC
            discordRPC = new DiscordRPC();
            discordRPC.connect();
            discordRPC.updatePresence("HYPE CLIENT", "In Launcher");
            
            // Start UI
            SwingUtilities.invokeLater(() -> {
                LauncherUI ui = new LauncherUI();
                ui.show();
            });
            
            logger.info("✅ HYPE CLIENT started successfully!");
            
        } catch (Exception e) {
            logger.error("❌ Failed to start HYPE CLIENT", e);
            JOptionPane.showMessageDialog(null, 
                "Error starting HYPE CLIENT: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    public static DiscordRPC getDiscordRPC() {
        return discordRPC;
    }
}
