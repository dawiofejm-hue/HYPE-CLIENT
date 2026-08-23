package com.hypeclient.launcher;

import com.hypeclient.auth.MicrosoftAuth;
import com.hypeclient.auth.OfflineAuth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Launches Minecraft with HYPE CLIENT cosmetics
 */
public class MinecraftLauncher {
    private static final Logger logger = LoggerFactory.getLogger(MinecraftLauncher.class);
    
    private static final String MINECRAFT_DIR = System.getProperty("user.home") + "/.minecraft";
    private static final String JAVA_VERSION = "11";
    
    private MicrosoftAuth microsoftAuth;
    private OfflineAuth offlineAuth;
    private String gameVersion = "1.20.1";

    public MinecraftLauncher() {
        logger.info("🎮 Initializing Minecraft Launcher...");
    }

    /**
     * Launch Minecraft with Microsoft account
     */
    public void launchWithMicrosoft(String authCode) {
        microsoftAuth = new MicrosoftAuth();
        
        if (!microsoftAuth.authenticate(authCode)) {
            logger.error("❌ Microsoft authentication failed");
            return;
        }

        launch(
            microsoftAuth.getUsername(),
            microsoftAuth.getUUID(),
            microsoftAuth.getAccessToken()
        );
    }

    /**
     * Launch Minecraft offline
     */
    public void launchOffline(String username) {
        offlineAuth = new OfflineAuth();
        offlineAuth.authenticate(username);

        if (!offlineAuth.isValid()) {
            logger.error("❌ Offline authentication failed");
            return;
        }

        launch(
            offlineAuth.getUsername(),
            offlineAuth.getUUID(),
            null
        );
    }

    /**
     * Main launch method
     */
    private void launch(String username, String uuid, String accessToken) {
        try {
            logger.info("🚀 Launching Minecraft for: " + username);
            
            // Get Java path
            String javaPath = findJava();
            if (javaPath == null) {
                logger.error("❌ Java not found!");
                return;
            }

            // Build launch arguments
            List<String> command = new ArrayList<>();
            command.add(javaPath);
            
            // JVM Arguments
            command.add("-Xmx2G");
            command.add("-Xms1G");
            command.add("-XX:+UseG1GC");
            command.add("-Dfile.encoding=UTF-8");
            command.add("-Duser.home=" + System.getProperty("user.home"));
            
            // Game arguments
            command.add("-cp");
            command.add(getClasspath());
            command.add("net.minecraft.client.main.Main");
            
            // Auth arguments
            command.add("--username");
            command.add(username);
            command.add("--uuid");
            command.add(uuid);
            
            if (accessToken != null) {
                command.add("--accessToken");
                command.add(accessToken);
                command.add("--userType");
                command.add("msa");
            } else {
                command.add("--userType");
                command.add("legacy");
            }
            
            // Version and assets
            command.add("--version");
            command.add(gameVersion);
            command.add("--assetIndex");
            command.add(gameVersion);
            command.add("--assetsDir");
            command.add(MINECRAFT_DIR + "/assets");
            command.add("--gameDir");
            command.add(MINECRAFT_DIR);
            
            logger.info("✅ Minecraft launching...");
            
            ProcessBuilder pb = new ProcessBuilder(command);
            pb.directory(new File(MINECRAFT_DIR));
            pb.redirectErrorStream(true);
            
            Process process = pb.start();
            
            // Log Minecraft output
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream())
            );
            String line;
            while ((line = reader.readLine()) != null) {
                logger.info("[MC] " + line);
            }
            
            process.waitFor();
            logger.info("✅ Minecraft closed");
            
        } catch (Exception e) {
            logger.error("❌ Launch failed", e);
        }
    }

    private String findJava() {
        String javaHome = System.getenv("JAVA_HOME");
        if (javaHome != null) {
            String javaPath = javaHome + "/bin/java";
            if (new File(javaPath).exists()) {
                return javaPath;
            }
        }
        
        // Try default system java
        try {
            new ProcessBuilder("java", "-version").start();
            return "java";
        } catch (Exception e) {
            return null;
        }
    }

    private String getClasspath() {
        // In production, this would include all Minecraft libraries
        // For now, return basic classpath
        return MINECRAFT_DIR + "/libraries/*:" + MINECRAFT_DIR + "/versions/" + gameVersion + "/" + gameVersion + ".jar";
    }

    public void setGameVersion(String version) {
        this.gameVersion = version;
    }
}
