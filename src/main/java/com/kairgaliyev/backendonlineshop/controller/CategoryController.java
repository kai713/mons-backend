package com.kairgaliyev.backendonlineshop.controller;

import com.kairgaliyev.backendonlineshop.dto.Response;
import com.kairgaliyev.backendonlineshop.entity.CategoryEntity;
import com.kairgaliyev.backendonlineshop.service.intreface.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/category")
@RequiredArgsConstructor
public class CategoryController {

    private final ICategoryService categoryService;

    @GetMapping
    public ResponseEntity<Response> getAllCategories() {
        Response response = categoryService.getAllCategories();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getCategory(@PathVariable("id") Long id) {
        Response response = categoryService.getCategoryById(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> createCategory(@RequestBody CategoryEntity category) {
        Response response = categoryService.createCategory(category);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> deleteCategory(@PathVariable Long id) {
        Response response = categoryService.deleteCategory(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/addProduct")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> addProductCategory(
            @RequestParam Long productId,
            @RequestParam Long categoryId
    ){
        Response response = categoryService.addProductToCategory(productId, categoryId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
