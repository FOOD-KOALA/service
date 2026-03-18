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

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    // 1. Creare comandă (Din Request în Entity)
    public Order createOrder(OrderCreateRequest request) {
        Order order = new Order();
        order.setCustomerId(request.getCustomerId());
        order.setRestaurantId(request.getRestaurantId());
        order.setItems(request.getItems());
        order.setTotalPrice(request.getTotalPrice());
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus("PENDING"); 

        return orderRepository.save(order);
    }

    // 2. Obține comandă după ID (Aruncă eroare dacă nu există pt. status 404)
    public Order getOrderById(String id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Comanda cu ID-ul " + id + " nu exista."));
    }

    // 3. Obține toate comenzile unui client
    public List<Order> getCustomerOrders(String customerId) {
        return orderRepository.findByCustomerId(customerId); // Trebuie sa adaugi metoda asta in OrderRepository!
    }

    // 4. Actualizează statusul comenzii
    public Order updateOrderStatus(String id, String newStatus) {
        Order order = getOrderById(id);
        
        // Extra Business Logic: Validăm statusul (doar exemplu)
        if (order.getStatus().equals("DELIVERED")) {
            throw new IllegalStateException("Nu poti modifica o comanda deja livrata!");
        }
        
        order.setStatus(newStatus.toUpperCase());
        return orderRepository.save(order);
    }
}