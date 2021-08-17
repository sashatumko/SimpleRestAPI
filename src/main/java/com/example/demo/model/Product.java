package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue()
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "brand")
    private String brand;

    @ElementCollection
    @CollectionTable(name = "tags", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "tags")
    private List<String> tags;

    @Column(name = "category")
    private String category;

    @Column(name = "created_at")
    private String createdAt;

    public Product(@JsonProperty("id") UUID id,
                   @JsonProperty("name") String name,
                   @JsonProperty("description") String description,
                   @JsonProperty("brand") String brand,
                   @JsonProperty("tags") List<String> tags,
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

    public Product() {}

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

    public List<String> getTags() { return tags; }

    public String getCategory() { return category; }

    public String getCreatedAt() { return createdAt; }

    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
