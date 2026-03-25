package ro.unibuc.prodeng.service;

import ro.unibuc.prodeng.service.entity.Order;
import ro.unibuc.prodeng.repository.OrderRepository;
import ro.unibuc.prodeng.request.OrderCreateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    private Order testOrder;
    private OrderCreateRequest testRequest;

    @BeforeEach
    void setUp() {
        // Se rulează înaintea fiecărui test pentru a pregăti datele (Arrange)
        testOrder = new Order();
        testOrder.setId("order-123");
        testOrder.setCustomerId("user-1");
        testOrder.setRestaurantId("rest-1");
        testOrder.setTotalPrice(45.0);
        testOrder.setStatus("PENDING");

        testRequest = new OrderCreateRequest();
        testRequest.setCustomerId("user-1");
        testRequest.setRestaurantId("rest-1");
        testRequest.setItems(Arrays.asList("Pizza"));
        testRequest.setTotalPrice(45.0);
    }

    // --- TESTE PENTRU CREARE ---

    @Test
    void testCreateOrder_ValidRequest_ReturnsSavedOrder() {
        // Arrange
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);

        // Act
        Order result = orderService.createOrder(testRequest);

        // Assert
        assertNotNull(result);
        assertEquals("PENDING", result.getStatus());
        assertEquals("order-123", result.getId());
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    // --- TESTE PENTRU GĂSIRE DUPĂ ID ---

    @Test
    void testGetOrderById_ExistingId_ReturnsOrder() {
        // Arrange
        when(orderRepository.findById("order-123")).thenReturn(Optional.of(testOrder));

        // Act
        Order result = orderService.getOrderById("order-123");

        // Assert
        assertNotNull(result);
        assertEquals("user-1", result.getCustomerId());
        verify(orderRepository, times(1)).findById("order-123");
    }

    @Test
    void testGetOrderById_NonExistingId_ThrowsException() {
        // Arrange
        when(orderRepository.findById("invalid-id")).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(NoSuchElementException.class, 
            () -> orderService.getOrderById("invalid-id"));
            
        assertTrue(exception.getMessage().contains("nu exista"));
        verify(orderRepository, times(1)).findById("invalid-id");
    }

    // --- TESTE PENTRU ACTUALIZARE STATUS (Business Logic) ---

    @Test
    void testUpdateOrderStatus_ValidStatusAndOrderPending_ReturnsUpdatedOrder() {
        // Arrange
        when(orderRepository.findById("order-123")).thenReturn(Optional.of(testOrder));
        
        Order updatedOrder = new Order();
        updatedOrder.setStatus("PREPARING");
        when(orderRepository.save(any(Order.class))).thenReturn(updatedOrder);

        // Act
        Order result = orderService.updateOrderStatus("order-123", "PREPARING");

        // Assert
        assertEquals("PREPARING", result.getStatus());
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void testUpdateOrderStatus_OrderAlreadyDelivered_ThrowsException() {
        // Arrange
        testOrder.setStatus("DELIVERED");
        when(orderRepository.findById("order-123")).thenReturn(Optional.of(testOrder));

        // Act & Assert
        Exception exception = assertThrows(IllegalStateException.class, 
            () -> orderService.updateOrderStatus("order-123", "CANCELLED"));
            
        assertTrue(exception.getMessage().contains("Nu poti modifica"));
        verify(orderRepository, never()).save(any(Order.class));
    }
}