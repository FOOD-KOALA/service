package ro.unibuc.prodeng.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public class OrderCreateRequest {

    @NotBlank(message = "ID-ul clientului este obligatoriu")
    private String customerId;

    @NotBlank(message = "ID-ul restaurantului este obligatoriu")
    private String restaurantId;

    @NotEmpty(message = "Comanda trebuie sa contina produse")
    private List<String> items;

    @NotNull(message = "Pretul este obligatoriu")
    @Min(value = 20, message = "Comanda minima este de 20 RON")
    private Double totalPrice;

    
    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
    public String getRestaurantId() { return restaurantId; }
    public void setRestaurantId(String restaurantId) { this.restaurantId = restaurantId; }
    public List<String> getItems() { return items; }
    public void setItems(List<String> items) { this.items = items; }
    public Double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(Double totalPrice) { this.totalPrice = totalPrice; }
}