package com.hypeclient.cosmetics;

/**
 * Hats cosmetic item
 */
public class Hats {
    private String id;
    private String name;
    private String description;
    private String modelPath;

    public Hats(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.modelPath = "assets/cosmetics/hats/" + id + ".obj";
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getModelPath() {
        return modelPath;
    }

    @Override
    public String toString() {
        return "Hats{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
