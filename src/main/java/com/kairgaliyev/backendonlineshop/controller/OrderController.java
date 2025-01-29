package com.kairgaliyev.backendonlineshop.controller;

import com.kairgaliyev.backendonlineshop.dto.Response;
import com.kairgaliyev.backendonlineshop.dto.StatusRoleRequest;
import com.kairgaliyev.backendonlineshop.service.intreface.IOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final IOrderService orderService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<Response> getUserOrders(@PathVariable Long userId) {
        Response response = orderService.getUserOrders(userId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/create")
    public ResponseEntity<Response> createOrder(@RequestParam Long userId) {
        Response response = orderService.createOrder(userId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/{orderId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> getOrder(@PathVariable Long orderId) {
        Response response = orderService.getOrder(orderId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/{orderId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long orderId) {
        orderService.deleteOrder(orderId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/status")
    public ResponseEntity<Response> updateOrderStatus(
            @RequestParam Long orderId,
            @RequestBody StatusRoleRequest status) {
        Response response = orderService.updateOrderStatus(orderId, status);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/orders")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> getOrders() {
        Response response = orderService.getAllOrders();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
