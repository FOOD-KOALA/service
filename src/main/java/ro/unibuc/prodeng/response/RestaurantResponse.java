package ro.unibuc.prodeng.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantResponse {
    private String id;
    private String name;
    private String address;
    private String cuisineType;
    private Double rating;
}