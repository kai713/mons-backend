package com.kairgaliyev.backendonlineshop.dto;

import com.kairgaliyev.backendonlineshop.model.Product;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CategoryDTO implements Serializable {
    private Long id;

    private String name;

    private List<Product> products = new ArrayList<>();
}
