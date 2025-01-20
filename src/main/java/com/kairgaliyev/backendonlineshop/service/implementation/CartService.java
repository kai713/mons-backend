package com.kairgaliyev.backendonlineshop.service.implementation;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartService implements ICartService {

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CartItemRepository cartItemRepository;

    // Получение корзины пользователя
    public Response getCartById(Long userId) {

        Response response = new Response();

        try {
            Cart cart = cartRepository.findByUserId(userId)
                    .orElseThrow(() -> new MyException("Корзина не найдена"));

            response.setCart(Utils.mapCartEntityToCartDTO(cart));
            response.setMessage("successful");
            response.setStatusCode(200);

        } catch (Exception e) {
            response.setStatusCode(404);
            response.setMessage("error get Cart by id " + e.getMessage());
        }
        return response;
    }

    // Добавление товара в корзину
    public Response addProduct(Long userId, Long productId, int quantity) {

        Response response = new Response();

        try {
            //Do not need due to userId will give by securityContext ???
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
                cartItem = new CartItem(cart, product, quantity);
                cartItemRepository.save(cartItem);
                cart.getCartItems().add(cartItem);
            }

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

    // Удаление товара из корзины
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

        } catch (MyException e) {
            response.setStatusCode(404);
            response.setMessage("error remove product in cart " + e.getMessage());

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("error remove product in cart " + e.getMessage());

        }
        return response;
    }

    // Очистка корзины
    public void clearCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new MyException("Cart not found"));

        cart.getCartItems().clear();
        cartRepository.save(cart);
    }

    // Изменение количества товара в корзине
    public Response updateProductQuantity(Long userId, Long productId, int quantity) {

        Response response = new Response();

        try {
            Cart cart = cartRepository.findByUserId(userId)
                    .orElseThrow(() -> new MyException("Cart not found"));

            CartItem cartItem = cart.getCartItems().stream()
                    .filter(item -> item.getProduct().getId().equals(productId))
                    .findFirst()
                    .orElseThrow(() -> new MyException("Product not found in cart"));

            if (quantity <= 0) {
                cart.getCartItems().remove(cartItem);
            } else {
                cartItem.setQuantity(quantity);
            }

            cartRepository.save(cart);
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
