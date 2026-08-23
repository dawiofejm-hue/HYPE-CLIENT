package com.hypeclient;

import com.hypeclient.api.CosmeticsAPIServer;
import com.hypeclient.web.WebDashboardServer;
import com.hypeclient.ui.LauncherUI;
import com.hypeclient.discord.DiscordRPC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;

/**
 * HYPE CLIENT - Advanced Minecraft Launcher
 * Features: Microsoft OAuth, Offline Mode, Cosmetics, Discord RPC, Web Dashboard, REST API
 */
public class HypeClientLauncher {
    private static final Logger logger = LoggerFactory.getLogger(HypeClientLauncher.class);
    
    private static CosmeticsAPIServer apiServer;
    private static WebDashboardServer webServer;
    private static DiscordRPC discordRPC;

    public static void main(String[] args) {
        logger.info("🚀 Starting HYPE CLIENT v1.0.0...");
        
        try {
            // Initialize Discord RPC
            logger.info("🎮 Initializing Discord RPC...");
            discordRPC = new DiscordRPC();
            discordRPC.connect();
            discordRPC.updatePresence("HYPE CLIENT", "In Launcher");
            
            // Start REST API Server (Port 8888)
            logger.info("🌐 Starting REST API Server on port 8888...");
            apiServer = new CosmeticsAPIServer(null);
            apiServer.start();
            
            // Start Web Dashboard Server (Port 3000)
            logger.info("🌐 Starting Web Dashboard on port 3000...");
            webServer = new WebDashboardServer();
            webServer.start();
            
            // Start GUI
            logger.info("🖥️ Starting GUI...");
            SwingUtilities.invokeLater(() -> {
                LauncherUI ui = new LauncherUI();
                ui.show();
            });
            
            logger.info("✅ HYPE CLIENT Ready!");
            logger.info("📊 REST API: http://localhost:8888");
            logger.info("🌐 Web Dashboard: http://localhost:3000");
            logger.info("🎮 Discord RPC: Connected");
            
            // Add shutdown hook
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                logger.info("🛑 Shutting down HYPE CLIENT...");
                if (apiServer != null) apiServer.stop();
                if (webServer != null) webServer.stop();
                if (discordRPC != null) discordRPC.disconnect();
                logger.info("✅ HYPE CLIENT stopped");
            }));
            
        } catch (Exception e) {
            logger.error("❌ Failed to start HYPE CLIENT", e);
            JOptionPane.showMessageDialog(null, 
                "Error starting HYPE CLIENT: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    public static DiscordRPC getDiscordRPC() {
        return discordRPC;
    }
}
