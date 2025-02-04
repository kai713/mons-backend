package com.kairgaliyev.backendonlineshop.service.intreface;

import com.kairgaliyev.backendonlineshop.dto.ProductDTO;
import com.kairgaliyev.backendonlineshop.dto.Response;
import org.springframework.web.multipart.MultipartFile;

public interface IProductService {
    Response addProduct(ProductDTO productDTO, MultipartFile file);

    Response updateProduct(ProductDTO productDTO, MultipartFile file);

    Response deleteProduct(String productId);

    Response getAllProducts();

    Response getProductById(String productId);

    Response searchProduct(String input);
}
