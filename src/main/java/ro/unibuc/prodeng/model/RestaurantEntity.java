package ro.unibuc.prodeng.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Document(collection = "restaurants")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantEntity {
    @Id
    private String id;
    private String name;
    private String address;
    private String cuisineType;
    private Double rating;
}