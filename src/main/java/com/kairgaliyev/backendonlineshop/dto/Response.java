package com.kairgaliyev.backendonlineshop.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@ToString
public class Response {
    private int statusCode;
    private String message;

    private String token;
    private String refreshToken;
    private String role;
    private String expirationTime;
    private String ConfirmationCode;

    private UserDTO user;
    private List<UserDTO> userList;

    private ProductDTO product;
    private List<ProductDTO> productList;

    private OrderDTO order;
    private List<OrderDTO> orderList;

    private CartDTO cart;
    private List<CartDTO> cartList;

    private CategoryDTO category;
    private List<CategoryDTO> categoryList;
}
