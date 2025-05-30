package com.kairgaliyev.backendonlineshop.service.intreface;

import com.kairgaliyev.backendonlineshop.dto.ProductDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IProductService {
    ProductDto addProduct(ProductDto productDTO, MultipartFile file);

    ProductDto updateProduct(ProductDto productDTO, MultipartFile file);

    void deleteProduct(String productId);

    List<ProductDto> getAllProducts();

    ProductDto getProductById(String productId);

    ProductDto searchProduct(String input);
}
