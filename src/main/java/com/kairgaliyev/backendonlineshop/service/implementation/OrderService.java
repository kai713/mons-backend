package com.kairgaliyev.backendonlineshop.service.implementation;

import com.kairgaliyev.backendonlineshop.dto.OrderDTO;
import com.kairgaliyev.backendonlineshop.dto.Response;
import com.kairgaliyev.backendonlineshop.dto.StatusRoleRequest;
import com.kairgaliyev.backendonlineshop.entity.CartEntity;
import com.kairgaliyev.backendonlineshop.entity.OrderEntity;
import com.kairgaliyev.backendonlineshop.entity.UserEntity;
import com.kairgaliyev.backendonlineshop.enums.OrderStatus;
import com.kairgaliyev.backendonlineshop.exception.MyException;
import com.kairgaliyev.backendonlineshop.repository.CartRepository;
import com.kairgaliyev.backendonlineshop.repository.OrderRepository;
import com.kairgaliyev.backendonlineshop.repository.UserRepository;
import com.kairgaliyev.backendonlineshop.service.intreface.IOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {
    private static final String USER_ORDERS_CACHE_KEY = "userOrders:";
    private static final String ORDER_CACHE_KEY = "order:";
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CartService cartService;
    private final CartRepository cartRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    public Response getUserOrders(Long userId) {
        Response response = new Response();

        List<OrderDTO> ordersFromCache = (List<OrderDTO>) redisTemplate.opsForValue().get(USER_ORDERS_CACHE_KEY + userId);

        if (ordersFromCache != null) {
            response.setStatusCode(200);
            response.setMessage("Success");
            response.setOrderList(ordersFromCache);
            return response;
        }

        List<OrderEntity> orderEntities = orderRepository.findByUserId(userId);

//        List<OrderDTO> orderDTOS = Utils.mapOrderListEntityToOrderListDTO(orderEntities);

//        response.setOrderList(orderDTOS);
        response.setStatusCode(200);
        response.setMessage("Success");

//        redisTemplate.opsForValue().set(USER_ORDERS_CACHE_KEY + userId, orderDTOS, Duration.ofMinutes(10));

        return response;
    }

    public Response createOrder(Long userId) {
        Response response = new Response();

        try {
            UserEntity user = userRepository.findById(userId)
                    .orElseThrow(() -> new MyException("User not found"));

            CartEntity cart = cartRepository.findByUserId(userId)
                    .orElseThrow(() -> new MyException("Cart not found"));

//            if (cart.getCartItems().isEmpty()) {
//                throw new MyException("Cart empty, order can not be created");
//            }

            OrderEntity orderEntity = new OrderEntity();
            orderEntity.setUser(user);
            orderEntity.setOrderStatus(OrderStatus.PURCHASE);

//            List<OrderItemEntity> orderItemEntities = cart.getCartItems().stream()
//                    .map(cartItem -> new OrderItemEntity(orderEntity, cartItem.getProduct(), cartItem.getQuantity()))
//                    .collect(Collectors.toList());
//
//            orderEntity.setOrderItems(orderItemEntities);
//
//            cartService.clearCart(userId);
//
//            orderEntity = orderRepository.save(orderEntity);
//
//            redisTemplate.delete(USER_ORDERS_CACHE_KEY + userId);
//
//            response.setStatusCode(200);
//            response.setMessage("Success");
//            response.setOrder(Utils.mapOrderEntityToOrderDTO(orderEntity));

        } catch (MyException e) {
            response.setStatusCode(404);
            response.setMessage("Error save order " + e.getMessage());

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error save order " + e.getMessage());
        }

        return response;
    }

    public Response getOrder(Long orderId) {
        Response response = new Response();

        try {
            OrderDTO orderFromCache = (OrderDTO) redisTemplate.opsForValue().get(ORDER_CACHE_KEY + orderId);

            if (orderFromCache != null) {
                response.setStatusCode(200);
                response.setMessage("Success");
                response.setOrder(orderFromCache);
                return response;
            }

            OrderEntity orderEntity = orderRepository.findById(orderId)
                    .orElseThrow(() -> new MyException("Order not found"));

//            OrderDTO orderDTOS = Utils.mapOrderEntityToOrderDTO(orderEntity);

//            redisTemplate.opsForValue().set(ORDER_CACHE_KEY + orderId, orderDTOS, Duration.ofMinutes(10));

            response.setStatusCode(200);
            response.setMessage("Success");
//            response.setOrder(orderDTOS);

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
        try {
            OrderEntity orderEntity = orderRepository.findById(orderId)
                    .orElseThrow(() -> new MyException("Order not found"));
            orderRepository.delete(orderEntity);

            redisTemplate.delete(ORDER_CACHE_KEY + orderId);
            redisTemplate.delete(USER_ORDERS_CACHE_KEY + orderId);

        } catch (MyException e) {
            System.out.println(e.getMessage());
        }
    }

    // Обновление статуса заказа
    public Response updateOrderStatus(Long orderId, StatusRoleRequest status) {
        Response response = new Response();

        try {
            OrderEntity orderEntity = orderRepository.findById(orderId)
                    .orElseThrow(() -> new MyException("Order not found"));

            orderEntity.setOrderStatus(status.getOrderStatusEnum());
            OrderEntity orderEntity1 = orderRepository.save(orderEntity);
            response.setStatusCode(200);
            response.setMessage("Success");
//            response.setOrder(Utils.mapOrderEntityToOrderDTO(orderEntity1));

            redisTemplate.delete(ORDER_CACHE_KEY + orderId);
            redisTemplate.delete(USER_ORDERS_CACHE_KEY + orderId);

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
//        response.setOrderList(Utils.mapOrderListEntityToOrderListDTO(orderRepository.findAll()));

        return response;
    }

}
