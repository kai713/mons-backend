package com.kairgaliyev.backendonlineshop.service.implementation;

import com.kairgaliyev.backendonlineshop.dto.CartDTO;
import com.kairgaliyev.backendonlineshop.dto.Response;
import com.kairgaliyev.backendonlineshop.exception.MyException;
import com.kairgaliyev.backendonlineshop.model.Cart;
import com.kairgaliyev.backendonlineshop.model.CartItem;
import com.kairgaliyev.backendonlineshop.model.Product;
import com.kairgaliyev.backendonlineshop.repository.CartItemRepository;
import com.kairgaliyev.backendonlineshop.repository.CartRepository;
import com.kairgaliyev.backendonlineshop.repository.ProductRepository;
import com.kairgaliyev.backendonlineshop.service.intreface.ICartService;
import com.kairgaliyev.backendonlineshop.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

//TODO: review and if need refactor logic.
//TODO: refactor check is the product in stock? = is quantity < stock
@Service
@RequiredArgsConstructor
public class CartService implements ICartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String CART_CACHE_KEY = "cart:";

    public Response getCartById(Long userId) {

        Response response = new Response();

        try {
            CartDTO cartDTO = (CartDTO) redisTemplate.opsForValue().get(CART_CACHE_KEY + userId);
            if (cartDTO != null) {
                response.setMessage("successful");
                response.setStatusCode(200);
                response.setCart(cartDTO);
            }
            Cart cart = cartRepository.findByUserId(userId)
                    .orElseThrow(() -> new MyException("Корзина не найдена"));
            cartDTO = Utils.mapCartEntityToCartDTO(cart);

            redisTemplate.opsForValue().set(CART_CACHE_KEY + userId, cartDTO, Duration.ofMinutes(10));

            response.setCart(cartDTO);
            response.setMessage("successful");
            response.setStatusCode(200);
        } catch (Exception e) {
            response.setStatusCode(404);
            response.setMessage("error get Cart by id " + e.getMessage());
        }
        return response;
    }

    public Response addProduct(Long userId, Long productId, int quantity) {

        Response response = new Response();

        try {
            Cart cart = cartRepository.findByUserId(userId)
                    .orElseThrow(() -> new MyException("Cart not found"));

            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new MyException("product not found"));

            CartItem cartItem = cart.getCartItems().stream()
                    .filter(item -> item.getProduct().getId().equals(productId))
                    .findFirst()
                    .orElse(null);

            if (cartItem != null) {
                cartItem.setQuantity(cartItem.getQuantity() + quantity);
                cartItemRepository.save(cartItem);
            } else {
                cartItem = new CartItem(cart, product, quantity, product.getName(), product.getPrice(), product.getImageUrl());
                cartItemRepository.save(cartItem);
                cart.getCartItems().add(cartItem);
            }

            redisTemplate.delete(CART_CACHE_KEY + userId);

//            cartRepository.save(cart);

            response.setStatusCode(200);
            response.setMessage("successful");
            response.setCart(Utils.mapCartEntityToCartDTO(cart));

        } catch (MyException e) {
            response.setStatusCode(404);
            response.setMessage("error add product in cart " + e.getMessage());

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("error add product in cart " + e.getMessage());

        }
        return response;
    }

    public Response removeProduct(Long userId, Long productId) {

        Response response = new Response();

        try {
            Cart cart = cartRepository.findByUserId(userId)
                    .orElseThrow(() -> new MyException("cart not found"));
            cart.getCartItems().removeIf(item -> item.getProduct().getId().equals(productId));

            cartRepository.save(cart);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setCart(Utils.mapCartEntityToCartDTO(cart));

            redisTemplate.delete(CART_CACHE_KEY + userId);

        } catch (MyException e) {
            response.setStatusCode(404);
            response.setMessage("error remove product in cart " + e.getMessage());

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("error remove product in cart " + e.getMessage());

        }
        return response;
    }

    public void clearCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new MyException("Cart not found"));

        redisTemplate.delete(CART_CACHE_KEY + userId);

        cart.getCartItems().clear();
        cartRepository.save(cart);
    }

    public Response updateProductQuantity(Long userId, Long productId, int quantity) {

        Response response = new Response();

        try {
            Cart cart = cartRepository.findByUserId(userId)
                    .orElseThrow(() -> new MyException("Cart not found"));

            CartItem cartItem = cart.getCartItems().stream()
                    .filter(item -> item.getProduct().getId().equals(productId))
                    .findFirst()
                    .orElseThrow(() -> new MyException("Product not found in cart"));

//            cartItem.setQuantity(cartItem.getQuantity() + quantity);

            if (quantity <= 0) {
                cart.getCartItems().remove(cartItem);
            } else {
                cartItem.setQuantity(quantity);
            }

            cartRepository.save(cart);

            redisTemplate.delete(CART_CACHE_KEY + userId);

            response.setStatusCode(200);
            response.setMessage("successful");
            response.setCart(Utils.mapCartEntityToCartDTO(cart));

        } catch (MyException e) {
            response.setStatusCode(404);
            response.setMessage("error update product quantity " + e.getMessage());

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("error update product quantity " + e.getMessage());

        }
        return response;
    }
}
