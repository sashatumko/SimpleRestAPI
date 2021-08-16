package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.UUID;

public class Product {

    private UUID id;
    private final String name;
    private final String description;
    private final String brand;
    private final ArrayList<String> tags;
    private final String category;
    private String createdAt;

    public Product(@JsonProperty("id") UUID id,
                   @JsonProperty("name") String name,
                   @JsonProperty("description") String description,
                   @JsonProperty("brand") String brand,
                   @JsonProperty("tags") ArrayList<String> tags,
                   @JsonProperty("category") String category,
                   @JsonProperty("created_at") String createdAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.brand = brand;
        this.tags = tags;
        this.category = category;
        this.createdAt = createdAt;
    }

    public void setId(UUID id) { this.id = id; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public UUID getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getDescription() { return description; }
    public String getBrand() { return brand; }
    public ArrayList<String> getTags() { return tags; }
    public String getCategory() { return category; }
    public String getCreatedAt() { return createdAt; }

    public String toJson() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
            return jsonString;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
