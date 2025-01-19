package com.kairgaliyev.backendonlineshop.service.intreface;

import com.kairgaliyev.backendonlineshop.dto.Response;
import com.kairgaliyev.backendonlineshop.dto.StatusRoleRequest;

public interface IOrderService {

    //TODO by curr user
    Response getUserOrders(Long userId);

    //TODO by curr user
    Response createOrder(Long userId);

    Response getOrder(Long orderId);

    void deleteOrder(Long orderId);

    // ?
    Response updateOrderStatus(Long orderId, StatusRoleRequest status);

    //TODO admin
    Response getAllOrders();

    //TODO in future Response getProductsInOrder(Long orderId);
}
