package ro.unibuc.prodeng.request;

import jakarta.validation.constraints.NotBlank;

public class OrderStatusUpdateRequest {
    @NotBlank(message = "Statusul nou este obligatoriu")
    private String status;

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}