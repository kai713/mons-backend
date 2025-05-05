package com.kairgaliyev.backendonlineshop.service.implementation;

import com.kairgaliyev.backendonlineshop.dto.CartDTO;
import com.kairgaliyev.backendonlineshop.dto.CartItemDTO;
import com.kairgaliyev.backendonlineshop.dto.IResponse;
import com.kairgaliyev.backendonlineshop.entity.CartEntity;
import com.kairgaliyev.backendonlineshop.entity.ProductEntity;
import com.kairgaliyev.backendonlineshop.exception.NotFoundException;
import com.kairgaliyev.backendonlineshop.repository.CartItemRepository;
import com.kairgaliyev.backendonlineshop.repository.CartRepository;
import com.kairgaliyev.backendonlineshop.repository.ProductRepository;
import com.kairgaliyev.backendonlineshop.service.LocalizationService;
import com.kairgaliyev.backendonlineshop.service.intreface.ICartService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

//TODO: review and if need refactor logic.
//TODO: refactor check is the product in stock? = is quantity < stock
@Service
@RequiredArgsConstructor
public class CartService implements ICartService {

    private static final String CART_CACHE_KEY = "cart:";
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final LocalizationService localizationService;

    public CartDTO getCartById(Long userId) {
        CartDTO cartDTO = (CartDTO) redisTemplate.opsForValue().get(CART_CACHE_KEY + userId);

        if (cartDTO != null) {
            return cartDTO;
        }

        CartEntity cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("Cart is not found"));

//        cartDTO = Utils.mapCartEntityToCartDTO(cart);

        redisTemplate.opsForValue().set(CART_CACHE_KEY + userId, cartDTO, Duration.ofMinutes(10));

//        return cartDTO;
        return null;
    }

    public CartItemDTO addProduct(Long userId, Long productId, int quantity) {
        //TODO: refactor N+1
//        CartEntity cart = cartRepository.findByUserId(userId)
//                .orElseThrow(() -> new NotFoundException("Cart not found"));

        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found "));

//        CartItemEntity cartItem = cart.getCartItemEntities().stream()
//                .filter(item -> item.getProduct().getId().equals(productId))
//                .findFirst()
//                .orElse(null);

//        if (cartItem != null) {
//            cartItem.setQuantity(cartItem.getQuantity() + quantity);
//            cartItemRepository.save(cartItem);
//        } else {
//            cartItem = new CartItem(cart, product, quantity, product.getName(), product.getPrice(), product.getImageUrl());
//            cartItemRepository.save(cartItem);
//            cart.getCartItems().add(cartItem);
//        }

        redisTemplate.delete(CART_CACHE_KEY + userId);

//        return Utils.mapCartItemEntityToDTO(cartItem);
        return null;
    }

    public IResponse removeProduct(Long userId, Long productId) {
        CartEntity cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("cart not found"));
//        cart.getCartItems().removeIf(item -> item.getProduct().getId().equals(productId));

        cartRepository.save(cart);

        redisTemplate.delete(CART_CACHE_KEY + userId);
        return new IResponse(localizationService.getMessage("success"));
    }

    public IResponse clearCart(Long userId) {
        CartEntity cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("Cart not found"));

        redisTemplate.delete(CART_CACHE_KEY + userId);

//        cart.getCartItems().clear();
        cartRepository.save(cart);
        return new IResponse(localizationService.getMessage("success"));
    }

    public CartItemDTO updateProductQuantity(Long userId, Long productId, int quantity) {
        CartEntity cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException(localizationService.getMessage("error.not.found.cart")));

//        CartItemEntity cartItem = cart.getCartItems().stream()
//                .filter(item -> item.getProduct().getId().equals(productId))
//                .findFirst()
//                .orElseThrow(() -> new NotFoundException("Product not found in cart"));

        //cartItem.setQuantity(cartItem.getQuantity() + quantity);

        if (quantity <= 0) {
//            cart.getCartItems().remove(cartItem);
        } else {
//            cartItem.setQuantity(quantity);
        }

        cartRepository.save(cart);

        redisTemplate.delete(CART_CACHE_KEY + userId);

//        return Utils.mapCartItemEntityToDTO(cartItem);
        return null;
    }
}
