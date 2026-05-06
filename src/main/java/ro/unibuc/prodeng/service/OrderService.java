package ro.unibuc.prodeng.service;

import ro.unibuc.prodeng.request.OrderCreateRequest;
import ro.unibuc.prodeng.service.entity.Order;
import ro.unibuc.prodeng.repository.OrderRepository;

import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final MetricsService metricsService;

    public OrderService(OrderRepository orderRepository, MetricsService metricsService) {
        this.orderRepository = orderRepository;
        this.metricsService = metricsService;
    }

    public Order createOrder(OrderCreateRequest request) {
        Order order = new Order();
        order.setCustomerId(request.getCustomerId());
        order.setRestaurantId(request.getRestaurantId());
        order.setItems(request.getItems());
        order.setTotalPrice(request.getTotalPrice());
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus("PENDING");

        Order saved = orderRepository.save(order);

        // METRICA 1 (business) + METRICA 5 (domain-specific)
        metricsService.recordOrderCreated(request.getItems().size());

        return saved;
    }

    public Order getOrderById(String id) {
        // METRICA 2 (performance) — măsoară cât durează fetch-ul
        return metricsService.recordOrderFetch(() ->
            orderRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Comanda cu ID-ul " + id + " nu exista."))
        );
    }

    public List<Order> getCustomerOrders(String customerId) {
        return orderRepository.findByCustomerId(customerId);
    }

    public Order updateOrderStatus(String id, String newStatus) {
        Order order = getOrderById(id);

        if (order.getStatus().equals("DELIVERED")) {
            throw new IllegalStateException("Nu poti modifica o comanda deja livrata!");
        }

        order.setStatus(newStatus.toUpperCase());
        return orderRepository.save(order);
    }
}