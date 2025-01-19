package com.kairgaliyev.backendonlineshop.utils;

import com.kairgaliyev.backendonlineshop.dto.*;
import com.kairgaliyev.backendonlineshop.model.*;

import java.security.SecureRandom;
import java.util.List;
import java.util.stream.Collectors;

//TODO use special mapper class
public class Utils {
    private static final String ALPHANUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom secureRandom = new SecureRandom();


    //conformation code for order etc
    public static String generateRandomConfirmationCode(int length) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(ALPHANUMERIC_STRING.length());
            char randomChar = ALPHANUMERIC_STRING.charAt(randomIndex);
            stringBuilder.append(randomChar);
        }
        return stringBuilder.toString();
    }

    public static UserDTO mapUserEntityToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();

        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setPhoneNumber(user.getPhoneNumber());
        userDTO.setRole(user.getRole());
        return userDTO;
    }

    public static List<UserDTO> mapUserListEntityToUserListDTO(List<User> userList) {
        return userList.stream().map(Utils::mapUserEntityToUserDTO).collect(Collectors.toList());
    }

    public static OrderDTO mapOrderEntityToOrderDTO(Order order) {
        OrderDTO orderDTO = new OrderDTO();

        orderDTO.setId(order.getId());
        orderDTO.setOrderStatus(order.getOrderStatus());
        orderDTO.setUpdatedAt(order.getUpdatedAt());
        orderDTO.setOrderItems(order.getOrderItems());
        return orderDTO;
    }

    public static List<OrderDTO> mapOrderListEntityToOrderListDTO(List<Order> orderList) {
        return orderList.stream().map(Utils::mapOrderEntityToOrderDTO).collect(Collectors.toList());
    }

    public static ProductDTO mapProductEntityToProductDTO(Product product) {
        ProductDTO productDTO = new ProductDTO();

        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setDescription(product.getDescription());
        productDTO.setPrice(product.getPrice());
        productDTO.setCategory(product.getCategory());
        return productDTO;
    }

    public static List<ProductDTO> mapProductListEntityToProductListDTO(List<Product> productList) {
        return productList.stream().map(Utils::mapProductEntityToProductDTO).collect(Collectors.toList());
    }

    public static CartDTO mapCartEntityToCartDTO(Cart cart) {
        CartDTO cartDTO = new CartDTO();

        cartDTO.setId(cart.getId());
        cartDTO.setCartItems(cart.getCartItems());
        return cartDTO;
    }

    public static List<CartDTO> mapCartListEntityToCartListDTO(List<Cart> cartList) {
        return cartList.stream().map(Utils::mapCartEntityToCartDTO).collect(Collectors.toList());
    }

    public static CategoryDTO mapCategoryEntityToCategoryDTO(Category category) {
        CategoryDTO categoryDTO = new CategoryDTO();

        categoryDTO.setId(category.getId());
        categoryDTO.setName(category.getName());
        categoryDTO.setProducts(category.getProducts());
        return categoryDTO;
    }

    public static List<CategoryDTO> mapCategoryListEntityToCategoryListDTO(List<Category> cartList) {
        return cartList.stream().map(Utils::mapCategoryEntityToCategoryDTO).collect(Collectors.toList());
    }
}
