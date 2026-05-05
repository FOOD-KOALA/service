package ro.unibuc.prodeng.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import ro.unibuc.prodeng.response.RestaurantResponse;
import ro.unibuc.prodeng.service.MetricsService;
import ro.unibuc.prodeng.service.RestaurantService;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private MetricsService metricsService;

    @GetMapping
    public List<RestaurantResponse> getAll() {
        return restaurantService.getAll();
    }

    @GetMapping("/{id}")
    public RestaurantResponse getById(@PathVariable @NonNull String id) {
        long start = System.nanoTime();
        try {
            return restaurantService.getById(id);
        } finally {
            metricsService.getRestaurantLookupTimer().record(System.nanoTime() - start, TimeUnit.NANOSECONDS);
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RestaurantResponse create(@Valid @RequestBody @NonNull RestaurantResponse restaurant) {
        try {
            RestaurantResponse created = restaurantService.create(restaurant);
            metricsService.recordRestaurantCreated();
            return created;
        } catch (Exception e) {
            metricsService.recordOperationFailed();
            throw e;
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable @NonNull String id) {
        try {
            restaurantService.delete(id);
            metricsService.recordRestaurantDeleted();
        } catch (Exception e) {
            metricsService.recordOperationFailed();
            throw e;
        }
    }
}
