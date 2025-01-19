package com.kairgaliyev.backendonlineshop.service.implementation;

import com.kairgaliyev.backendonlineshop.dto.Response;
import com.kairgaliyev.backendonlineshop.dto.StatusRoleRequest;
import com.kairgaliyev.backendonlineshop.enums.OrderStatus;
import com.kairgaliyev.backendonlineshop.exception.MyException;
import com.kairgaliyev.backendonlineshop.model.Cart;
import com.kairgaliyev.backendonlineshop.model.Order;
import com.kairgaliyev.backendonlineshop.model.OrderItem;
import com.kairgaliyev.backendonlineshop.model.User;
import com.kairgaliyev.backendonlineshop.repository.CartRepository;
import com.kairgaliyev.backendonlineshop.repository.OrderRepository;
import com.kairgaliyev.backendonlineshop.repository.UserRepository;
import com.kairgaliyev.backendonlineshop.service.intreface.IOrderService;
import com.kairgaliyev.backendonlineshop.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService implements IOrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartService cartService;
    @Autowired
    private CartRepository cartRepository;

    // Получение всех заказов пользователя
    public Response getUserOrders(Long userId) {
        Response response = new Response();

        List<Order> orders = orderRepository.findByUserId(userId);
        response.setStatusCode(200);
        response.setMessage("Success");
        response.setOrderList(Utils.mapOrderListEntityToOrderListDTO(orders));

        return response;
    }

    // Создание заказа на основе корзины
    public Response createOrder(Long userId) {
        Response response = new Response();

        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new MyException("User not found"));

            Cart cart = cartRepository.findByUserId(userId)
                    .orElseThrow(() -> new MyException("Cart not found"));

            if (cart.getCartItems().isEmpty()) {
                throw new MyException("Cart empty, order can not be created");
            }

            Order order = new Order();
            order.setUser(user);
            order.setOrderStatus(OrderStatus.PURCHASE);

            List<OrderItem> orderItems = cart.getCartItems().stream()
                    .map(cartItem -> new OrderItem(order, cartItem.getProduct(), cartItem.getQuantity()))
                    .collect(Collectors.toList());

            order.setOrderItems(orderItems);

            cartService.clearCart(userId); // Очистка корзины после оформления заказа

            Order order1 = orderRepository.save(order);

            response.setStatusCode(200);
            response.setMessage("Success");
            response.setOrder(Utils.mapOrderEntityToOrderDTO(order1));

        } catch (MyException e) {
            response.setStatusCode(404);
            response.setMessage("Error save order " + e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error save order " + e.getMessage());
        }

        return response;
    }

    // Получение заказа по ID
    public Response getOrder(Long orderId) {
        Response response = new Response();

        try {
            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new MyException("Order not found"));

            response.setStatusCode(200);
            response.setMessage("Success");
            response.setOrder(Utils.mapOrderEntityToOrderDTO(order));

        } catch (MyException e) {
            response.setStatusCode(404);
            response.setMessage("Error save order " + e.getMessage());

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error save order " + e.getMessage());
        }
        return response;
    }

    // Удаление заказа
    public void deleteOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new MyException("Order not found"));
        orderRepository.delete(order);
    }

    // Обновление статуса заказа
    public Response updateOrderStatus(Long orderId, StatusRoleRequest status) {
        Response response = new Response();

        try {
            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new MyException("Order not found"));

            order.setOrderStatus(status.getOrderStatusEnum());
            Order order1 = orderRepository.save(order);
            response.setStatusCode(200);
            response.setMessage("Success");
            response.setOrder(Utils.mapOrderEntityToOrderDTO(order1));
        } catch (MyException e) {
            response.setStatusCode(404);
            response.setMessage("Error save order " + e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error save order " + e.getMessage());
        }
        return response;
    }

    public Response getAllOrders() {
        Response response = new Response();

        response.setStatusCode(200);
        response.setMessage("Success");
        response.setOrderList(Utils.mapOrderListEntityToOrderListDTO(orderRepository.findAll()));

        return response;
    }

}
