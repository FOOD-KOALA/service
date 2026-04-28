package ro.unibuc.prodeng.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "menus")
public record MenuEntity(
    @Id
    String id,
    String name,
    String description,
    Double price,
    boolean available
) {
    // Constructor care setează automat 'available' pe true când creăm un meniu nou
    public MenuEntity(String name, String description, Double price) {
        this(null, name, description, price, true);
    }
}