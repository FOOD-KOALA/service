package ro.unibuc.prodeng.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateMenuRequest(
    @NotBlank(message = "Numele meniului este obligatoriu")
    String name,
    
    String description,
    
    @NotNull(message = "Pretul este obligatoriu")
    @Min(value = 0, message = "Pretul nu poate fi negativ")
    Double price
) {}