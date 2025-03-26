package com.kairgaliyev.backendonlineshop.service.intreface;

import com.kairgaliyev.backendonlineshop.dto.CartDTO;
import com.kairgaliyev.backendonlineshop.dto.CartItemDTO;
import com.kairgaliyev.backendonlineshop.dto.IResponse;

public interface ICartService {

    CartDTO getCartById(Long userId);

    CartItemDTO addProduct(Long userId, Long productId, int quantity);

    IResponse removeProduct(Long userId, Long productId);

    IResponse clearCart(Long userId);

    CartItemDTO updateProductQuantity(Long userId, Long productId, int quantity);
}
