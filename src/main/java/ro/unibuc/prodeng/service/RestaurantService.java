package ro.unibuc.prodeng.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ro.unibuc.prodeng.model.RestaurantEntity;
import ro.unibuc.prodeng.repository.RestaurantRepository;
import ro.unibuc.prodeng.response.RestaurantResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    public List<RestaurantResponse> getAll() {
        return restaurantRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public RestaurantResponse getById(String id) {
        return restaurantRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public RestaurantResponse create(RestaurantResponse dto) {
        RestaurantEntity entity = new RestaurantEntity(null, dto.getName(), dto.getAddress(), dto.getCuisineType(), dto.getRating());
        return mapToResponse(restaurantRepository.save(entity));
    }

    public void delete(String id) {
        if (!restaurantRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        restaurantRepository.deleteById(id);
    }

    private RestaurantResponse mapToResponse(RestaurantEntity entity) {
        return new RestaurantResponse(entity.getId(), entity.getName(), entity.getAddress(), entity.getCuisineType(), entity.getRating());
    }
}