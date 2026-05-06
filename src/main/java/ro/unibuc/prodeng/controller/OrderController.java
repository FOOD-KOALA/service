package ro.unibuc.prodeng.controller;

import ro.unibuc.prodeng.request.OrderCreateRequest;
import ro.unibuc.prodeng.request.OrderStatusUpdateRequest;
import ro.unibuc.prodeng.service.entity.Order;
import ro.unibuc.prodeng.service.MetricsService;
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
    private final MetricsService metricsService;

    public OrderController(OrderService orderService, MetricsService metricsService) {
        this.orderService = orderService;
        this.metricsService = metricsService;
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@Valid @RequestBody OrderCreateRequest request) {
        Order savedOrder = orderService.createOrder(request);
        return new ResponseEntity<>(savedOrder, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable String id) {
        try {
            return ResponseEntity.ok(orderService.getOrderById(id));
        } catch (NoSuchElementException e) {
            // METRICA 3 (error) — comanda nu a fost gasita
            metricsService.recordOrderNotFound();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Order>> getCustomerOrders(@PathVariable String customerId) {
        return ResponseEntity.ok(orderService.getCustomerOrders(customerId));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(
            @PathVariable String id,
            @Valid @RequestBody OrderStatusUpdateRequest request) {
        try {
            Order updatedOrder = orderService.updateOrderStatus(id, request.getStatus());
            return ResponseEntity.ok(updatedOrder);
        } catch (NoSuchElementException e) {
            metricsService.recordOrderNotFound();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}