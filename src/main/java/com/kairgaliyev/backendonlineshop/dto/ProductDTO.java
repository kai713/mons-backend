package com.kairgaliyev.backendonlineshop.dto;

import com.kairgaliyev.backendonlineshop.model.Category;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ProductDTO {
    private Long id;
    private String name;
    private String description;
    @Positive(message = "product price must be a positive value")
    @Positive(message = "product price must be a positive value")
    private Double price;
    private Integer stock;

    private Category category;

    //Boil code

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public @Positive(message = "product price must be a positive value") @Positive(message = "product price must be a positive value") Double getPrice() {
        return price;
    }

    public void setPrice(@Positive(message = "product price must be a positive value") @Positive(message = "product price must be a positive value") Double price) {
        this.price = price;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }
}
