package ro.unibuc.prodeng.service.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "orders")
public class Order {

    @Id
    private String id;

    @NotBlank(message = "ID-ul clientului este obligatoriu")
    private String customerId;

    @NotBlank(message = "ID-ul restaurantului este obligatoriu")
    private String restaurantId;

    @NotEmpty(message = "Comanda trebuie sa contina cel putin un produs")
    private List<String> items; // O listă simplă cu numele sau ID-urile produselor

    @NotNull(message = "Pretul total este obligatoriu")
    @Min(value = 1, message = "Pretul total trebuie sa fie mai mare decat 0")
    private Double totalPrice;

    private String status; // ex: PENDING, PREPARING, DELIVERED
    private LocalDateTime createdAt;

    // Getteri și Setteri
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public String getRestaurantId() { return restaurantId; }
    public void setRestaurantId(String restaurantId) { this.restaurantId = restaurantId; }

    public List<String> getItems() { return items; }
    public void setItems(List<String> items) { this.items = items; }

    public Double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(Double totalPrice) { this.totalPrice = totalPrice; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}