package com.kairgaliyev.backendonlineshop.service.intreface;

import com.kairgaliyev.backendonlineshop.dto.Response;

public interface ICartService {

    Response getCartById(Long userId);

    Response addProduct(Long userId, Long productId, int quantity);

    Response removeProduct(Long userId, Long productId);

    void clearCart(Long userId);

    Response updateProductQuantity(Long userId, Long productId, int quantity);
}
