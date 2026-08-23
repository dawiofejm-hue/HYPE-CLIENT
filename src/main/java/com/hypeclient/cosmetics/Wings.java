package com.hypeclient.cosmetics;

/**
 * Wings cosmetic item
 */
public class Wings {
    private String id;
    private String name;
    private String description;
    private String modelPath;

    public Wings(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.modelPath = "assets/cosmetics/wings/" + id + ".obj";
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
        return "Wings{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
