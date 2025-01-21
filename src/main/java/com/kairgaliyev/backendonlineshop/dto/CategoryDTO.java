package com.kairgaliyev.backendonlineshop.dto;

import com.kairgaliyev.backendonlineshop.model.Product;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class CategoryDTO implements Serializable {
    private Long id;

    private String name;

    private List<Product> products = new ArrayList<>();

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

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
