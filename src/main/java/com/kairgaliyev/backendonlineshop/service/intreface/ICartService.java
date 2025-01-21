package com.kairgaliyev.backendonlineshop.service.intreface;

import com.kairgaliyev.backendonlineshop.dto.Response;

public interface ICartService {

    //TODO curr user
    Response getCartById(Long userId);

    //TODO test + by current user + implement: logic is product in cart?
    Response addProduct(Long userId, Long productId, int quantity);

    //TODO curr user
    Response removeProduct(Long userId, Long productId);

    //TODO curr user
    void clearCart(Long userId);

    //TODO curr user
    Response updateProductQuantity(Long userId, Long productId, int quantity);
}
