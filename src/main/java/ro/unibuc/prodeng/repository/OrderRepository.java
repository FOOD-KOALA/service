package ro.unibuc.prodeng.repository;

import ro.unibuc.prodeng.service.entity.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends MongoRepository<Order, String> {
    // Spring generează codul automat. O metodă utilă pe viitor:
    List<Order> findByCustomerId(String customerId);
}
