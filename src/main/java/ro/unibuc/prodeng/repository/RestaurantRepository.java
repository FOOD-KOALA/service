package ro.unibuc.prodeng.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ro.unibuc.prodeng.model.RestaurantEntity;

@Repository
public interface RestaurantRepository extends MongoRepository<RestaurantEntity, String> {
}