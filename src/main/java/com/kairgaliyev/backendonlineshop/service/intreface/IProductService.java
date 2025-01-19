package com.kairgaliyev.backendonlineshop.service.intreface;

import com.kairgaliyev.backendonlineshop.dto.ProductDTO;
import com.kairgaliyev.backendonlineshop.dto.Response;

public interface IProductService {
    Response addProduct(ProductDTO productDTO);

    Response updateProduct(ProductDTO productDTO);

    Response deleteProduct(String productId);

    Response getAllProducts();

    Response getProductById(String productId);

    Response searchProduct(String input);
}
