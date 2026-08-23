package com.hypeclient.cosmetics;

/**
 * Pets cosmetic item
 */
public class Pets {
    private String id;
    private String name;
    private String description;
    private String modelPath;

    public Pets(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.modelPath = "assets/cosmetics/pets/" + id + ".obj";
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
        return "Pets{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
