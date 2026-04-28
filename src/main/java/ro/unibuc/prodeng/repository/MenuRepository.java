package ro.unibuc.prodeng.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ro.unibuc.prodeng.model.MenuEntity;

import java.util.List;

@Repository
public interface MenuRepository extends MongoRepository<MenuEntity, String> {
    
    // Spring Boot va genera automat codul pentru metoda asta!
    List<MenuEntity> findByAvailable(boolean available);
}