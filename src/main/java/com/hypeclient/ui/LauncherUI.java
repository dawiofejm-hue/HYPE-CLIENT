package com.hypeclient.ui;

import com.hypeclient.HypeClientLauncher;
import com.hypeclient.auth.MicrosoftAuth;
import com.hypeclient.auth.OfflineAuth;
import com.hypeclient.cosmetics.CosmeticManager;
import com.hypeclient.launcher.MinecraftLauncher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * HYPE CLIENT Launcher UI
 */
public class LauncherUI extends JFrame {
    private static final Logger logger = LoggerFactory.getLogger(LauncherUI.class);
    
    private JTabbedPane tabbedPane;
    private JPanel loginPanel;
    private JPanel cosmeticsPanel;
    private JPanel settingsPanel;
    
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton microsoftLoginButton;
    private JButton offlineLoginButton;
    private JButton launchButton;
    
    private CosmeticManager cosmeticManager;
    private MinecraftLauncher launcher;
    private String currentUsername;
    private boolean isMicrosoftAuth = false;

    public LauncherUI() {
        setTitle("HYPE CLIENT v1.0.0");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        
        cosmeticManager = new CosmeticManager();
        launcher = new MinecraftLauncher();
        
        setupUI();
    }

    private void setupUI() {
        tabbedPane = new JTabbedPane();
        
        // Login Tab
        loginPanel = createLoginPanel();
        tabbedPane.addTab("🔐 Login", loginPanel);
        
        // Cosmetics Tab
        cosmeticsPanel = createCosmeticsPanel();
        tabbedPane.addTab("🎨 Cosmetics", cosmeticsPanel);
        
        // Settings Tab
        settingsPanel = createSettingsPanel();
        tabbedPane.addTab("⚙️ Settings", settingsPanel);
        
        add(tabbedPane);
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Title
        JLabel titleLabel = new JLabel("HYPE CLIENT LOGIN");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);
        
        // Microsoft Auth
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        microsoftLoginButton = new JButton("🔐 Login with Microsoft Account");
        microsoftLoginButton.setPreferredSize(new Dimension(300, 40));
        microsoftLoginButton.addActionListener(e -> loginWithMicrosoft());
        panel.add(microsoftLoginButton, gbc);
        
        // Separator
        gbc.gridy = 2;
        panel.add(new JSeparator(), gbc);
        
        // Offline Auth
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        panel.add(new JLabel("Offline Username:"), gbc);
        
        gbc.gridx = 1;
        usernameField = new JTextField(15);
        panel.add(usernameField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        offlineLoginButton = new JButton("👤 Play Offline");
        offlineLoginButton.setPreferredSize(new Dimension(300, 40));
        offlineLoginButton.addActionListener(e -> loginOffline());
        panel.add(offlineLoginButton, gbc);
        
        // Launch Button
        gbc.gridy = 5;
        launchButton = new JButton("▶️ LAUNCH MINECRAFT");
        launchButton.setPreferredSize(new Dimension(300, 50));
        launchButton.setFont(new Font("Arial", Font.BOLD, 16));
        launchButton.setBackground(new Color(255, 100, 0));
        launchButton.setForeground(Color.WHITE);
        launchButton.setEnabled(false);
        launchButton.addActionListener(e -> launchMinecraft());
        panel.add(launchButton, gbc);
        
        return panel;
    }

    private JPanel createCosmeticsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 3));
        
        // Wings
        JPanel wingsPanel = new JPanel();
        wingsPanel.setBorder(BorderFactory.createTitledBorder("🪶 Wings"));
        wingsPanel.setLayout(new BoxLayout(wingsPanel, BoxLayout.Y_AXIS));
        
        for (var wings : cosmeticManager.getAvailableWings()) {
            JButton btn = new JButton(wings.getName());
            btn.addActionListener(e -> {
                if (currentUsername != null) {
                    cosmeticManager.equipWings(currentUsername, wings.getId());
                    JOptionPane.showMessageDialog(null, "Equipped: " + wings.getName());
                    updateDiscordPresence();
                }
            });
            wingsPanel.add(btn);
        }
        panel.add(new JScrollPane(wingsPanel));
        
        // Hats
        JPanel hatsPanel = new JPanel();
        hatsPanel.setBorder(BorderFactory.createTitledBorder("👑 Hats"));
        hatsPanel.setLayout(new BoxLayout(hatsPanel, BoxLayout.Y_AXIS));
        
        for (var hat : cosmeticManager.getAvailableHats()) {
            JButton btn = new JButton(hat.getName());
            btn.addActionListener(e -> {
                if (currentUsername != null) {
                    cosmeticManager.equipHat(currentUsername, hat.getId());
                    JOptionPane.showMessageDialog(null, "Equipped: " + hat.getName());
                    updateDiscordPresence();
                }
            });
            hatsPanel.add(btn);
        }
        panel.add(new JScrollPane(hatsPanel));
        
        // Pets
        JPanel petsPanel = new JPanel();
        petsPanel.setBorder(BorderFactory.createTitledBorder("🐉 Pets"));
        petsPanel.setLayout(new BoxLayout(petsPanel, BoxLayout.Y_AXIS));
        
        for (var pet : cosmeticManager.getAvailablePets()) {
            JButton btn = new JButton(pet.getName());
            btn.addActionListener(e -> {
                if (currentUsername != null) {
                    cosmeticManager.equipPet(currentUsername, pet.getId());
                    JOptionPane.showMessageDialog(null, "Equipped: " + pet.getName());
                    updateDiscordPresence();
                }
            });
            petsPanel.add(btn);
        }
        panel.add(new JScrollPane(petsPanel));
        
        return panel;
    }

    private JPanel createSettingsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel versionLabel = new JLabel("Minecraft Version: 1.20.1");
        panel.add(versionLabel);
        
        JLabel discordLabel = new JLabel("Discord RPC: Connected ✅");
        panel.add(discordLabel);
        
        JLabel pathLabel = new JLabel("Game Directory: " + System.getProperty("user.home") + "/.minecraft");
        panel.add(pathLabel);
        
        panel.add(Box.createVerticalGlue());
        
        JButton exitButton = new JButton("Exit HYPE CLIENT");
        exitButton.addActionListener(e -> System.exit(0));
        panel.add(exitButton);
        
        return panel;
    }

    private void loginWithMicrosoft() {
        logger.info("Starting Microsoft login...");
        MicrosoftAuth auth = new MicrosoftAuth();
        auth.startAuthFlow();
        
        String authCode = JOptionPane.showInputDialog(
            this,
            "Enter the authorization code from your browser:",
            "Microsoft OAuth"
        );
        
        if (authCode != null && auth.authenticate(authCode)) {
            currentUsername = auth.getUsername();
            isMicrosoftAuth = true;
            launchButton.setEnabled(true);
            JOptionPane.showMessageDialog(this, "✅ Welcome, " + currentUsername);
            updateDiscordPresence();
        }
    }

    private void loginOffline() {
        String username = usernameField.getText().trim();
        
        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(this, "❌ Please enter a username");
            return;
        }
        
        OfflineAuth auth = new OfflineAuth();
        auth.authenticate(username);
        
        currentUsername = auth.getUsername();
        isMicrosoftAuth = false;
        launchButton.setEnabled(true);
        JOptionPane.showMessageDialog(this, "✅ Welcome, " + currentUsername);
        updateDiscordPresence();
    }

    private void launchMinecraft() {
        if (currentUsername == null) {
            JOptionPane.showMessageDialog(this, "❌ Please login first");
            return;
        }
        
        logger.info("Launching Minecraft...");
        
        if (isMicrosoftAuth) {
            // Launch with Microsoft auth
            launcher.launchWithMicrosoft(null);
        } else {
            // Launch offline
            launcher.launchOffline(currentUsername);
        }
        
        updateDiscordPresence();
    }

    private void updateDiscordPresence() {
        if (currentUsername == null) return;
        
        var wings = cosmeticManager.getActiveWings(currentUsername);
        var hat = cosmeticManager.getActiveHat(currentUsername);
        var pet = cosmeticManager.getActivePet(currentUsername);
        
        StringBuilder cosmetics = new StringBuilder();
        if (wings != null) cosmetics.append("🪶 ").append(wings.getName()).append(" | ");
        if (hat != null) cosmetics.append("👑 ").append(hat.getName()).append(" | ");
        if (pet != null) cosmetics.append("🐉 ").append(pet.getName());
        
        HypeClientLauncher.getDiscordRPC().updateGameStatus(
            currentUsername,
            "Launcher",
            cosmetics.length() > 0 ? cosmetics.toString() : "No cosmetics equipped"
        );
    }

    public void show() {
        setVisible(true);
    }
}
