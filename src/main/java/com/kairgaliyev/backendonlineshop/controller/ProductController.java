package com.kairgaliyev.backendonlineshop.controller;

import com.kairgaliyev.backendonlineshop.dto.ProductDTO;
import com.kairgaliyev.backendonlineshop.dto.Response;
import com.kairgaliyev.backendonlineshop.service.intreface.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final IProductService productService;

    @GetMapping("/all")
    public ResponseEntity<Response> getAllProducts() {
        Response response = productService.getAllProducts();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> addProduct(@RequestPart("productDTO") ProductDTO productDTO,
                                               @RequestPart(value = "photo", required = false) MultipartFile image) {
        if (image == null) {
            return ResponseEntity.badRequest().build();
        }

        Response response = productService.addProduct(productDTO, image);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    //TODO check maybe smth will went wrong :(
    @PutMapping("/update")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> updateProduct(@RequestPart("productDTO") ProductDTO productDTO,
                                                  @RequestPart(value = "photo", required = false) MultipartFile image) {
        if (image != null) {
            return ResponseEntity.badRequest().build();
        }

        Response response = productService.updateProduct(productDTO, image);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getProductById(@PathVariable("id") String id) {
        Response response = productService.getProductById(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    //TODO fix: method is working but we get 403 code...
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> deleteProduct(@PathVariable("id") String id) {
        Response response = productService.deleteProduct(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/search")
    public ResponseEntity<Response> searchProduct(@RequestParam String input) {
        Response response = productService.searchProduct(input);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
