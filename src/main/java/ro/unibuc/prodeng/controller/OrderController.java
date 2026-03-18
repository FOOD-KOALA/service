package ro.unibuc.prodeng.controller;

import ro.unibuc.prodeng.request.OrderCreateRequest;
import ro.unibuc.prodeng.request.OrderStatusUpdateRequest;
import ro.unibuc.prodeng.service.entity.Order;
import ro.unibuc.prodeng.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // Endpoint 1: POST /api/orders (Creare)
    @PostMapping
    public ResponseEntity<Order> createOrder(@Valid @RequestBody OrderCreateRequest request) {
        Order savedOrder = orderService.createOrder(request);
        return new ResponseEntity<>(savedOrder, HttpStatus.CREATED); // Returneaza 201 Created
    }

    // Endpoint 2: GET /api/orders/{id} (Aducere o singura comanda)
    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable String id) {
        try {
            return ResponseEntity.ok(orderService.getOrderById(id));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); // Returneaza 404 Not Found
        }
    }

    // Endpoint 3: GET /api/orders/customer/{customerId} (Istoric client)
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Order>> getCustomerOrders(@PathVariable String customerId) {
        return ResponseEntity.ok(orderService.getCustomerOrders(customerId));
    }

    // Endpoint 4: PATCH /api/orders/{id}/status (Schimbare status)
    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(
            @PathVariable String id, 
            @Valid @RequestBody OrderStatusUpdateRequest request) {
        try {
            Order updatedOrder = orderService.updateOrderStatus(id, request.getStatus());
            return ResponseEntity.ok(updatedOrder);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage()); // Returneaza 400 Bad Request
        }
    }
}