package com.hypeclient.cosmetics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Manages all cosmetics: Wings, Hats, Pets
 */
public class CosmeticManager {
    private static final Logger logger = LoggerFactory.getLogger(CosmeticManager.class);
    
    private Map<String, Wings> activeWings = new HashMap<>();
    private Map<String, Hats> activeHats = new HashMap<>();
    private Map<String, Pets> activePets = new HashMap<>();
    
    private List<Wings> availableWings;
    private List<Hats> availableHats;
    private List<Pets> availablePets;

    public CosmeticManager() {
        logger.info("🎨 Initializing Cosmetic Manager...");
        initializeCosmetics();
    }

    private void initializeCosmetics() {
        // Initialize Wings
        availableWings = Arrays.asList(
            new Wings("angel_wings", "Angel Wings", "White feathered wings"),
            new Wings("demon_wings", "Demon Wings", "Dark bat-like wings"),
            new Wings("dragon_wings", "Dragon Wings", "Majestic dragon wings"),
            new Wings("fairy_wings", "Fairy Wings", "Sparkly pixie wings"),
            new Wings("phoenix_wings", "Phoenix Wings", "Flaming phoenix wings")
        );

        // Initialize Hats
        availableHats = Arrays.asList(
            new Hats("crown", "Golden Crown", "Luxury golden crown"),
            new Hats("wizard_hat", "Wizard Hat", "Enchanted wizard hat"),
            new Hats("horns", "Devil Horns", "Spiky devil horns"),
            new Hats("halo", "Halo", "Glowing halo"),
            new Hats("cat_ears", "Cat Ears", "Cute cat ears headband")
        );

        // Initialize Pets
        availablePets = Arrays.asList(
            new Pets("wolf", "Wolf", "Loyal wolf companion"),
            new Pets("dragon", "Dragon", "Mini dragon pet"),
            new Pets("phoenix", "Phoenix", "Fire phoenix pet"),
            new Pets("guardian", "Guardian", "Mystical guardian"),
            new Pets("knight_horse", "Knight Horse", "Noble white horse")
        );

        logger.info("✅ Loaded " + availableWings.size() + " wings");
        logger.info("✅ Loaded " + availableHats.size() + " hats");
        logger.info("✅ Loaded " + availablePets.size() + " pets");
    }

    public void equipWings(String username, String wingsId) {
        Wings wings = availableWings.stream()
            .filter(w -> w.getId().equals(wingsId))
            .findFirst()
            .orElse(null);
        
        if (wings != null) {
            activeWings.put(username, wings);
            logger.info("👤 " + username + " equipped: " + wings.getName());
        }
    }

    public void equipHat(String username, String hatId) {
        Hats hat = availableHats.stream()
            .filter(h -> h.getId().equals(hatId))
            .findFirst()
            .orElse(null);
        
        if (hat != null) {
            activeHats.put(username, hat);
            logger.info("👤 " + username + " equipped: " + hat.getName());
        }
    }

    public void equipPet(String username, String petId) {
        Pets pet = availablePets.stream()
            .filter(p -> p.getId().equals(petId))
            .findFirst()
            .orElse(null);
        
        if (pet != null) {
            activePets.put(username, pet);
            logger.info("👤 " + username + " equipped: " + pet.getName());
        }
    }

    public Wings getActiveWings(String username) {
        return activeWings.get(username);
    }

    public Hats getActiveHat(String username) {
        return activeHats.get(username);
    }

    public Pets getActivePet(String username) {
        return activePets.get(username);
    }

    public List<Wings> getAvailableWings() {
        return availableWings;
    }

    public List<Hats> getAvailableHats() {
        return availableHats;
    }

    public List<Pets> getAvailablePets() {
        return availablePets;
    }

    public Map<String, Object> getCosmeticsInfo(String username) {
        Map<String, Object> info = new HashMap<>();
        info.put("wings", getActiveWings(username));
        info.put("hat", getActiveHat(username));
        info.put("pet", getActivePet(username));
        return info;
    }
}
