package ro.unibuc.prodeng.service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Service;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class MetricsService {

    private final Counter restaurantCreatedCounter;
    private final Counter operationFailedCounter;
    private final Timer restaurantLookupTimer;
    private final AtomicInteger activeRestaurants = new AtomicInteger(0);
    private final Counter restaurantDeletedCounter;

    public MetricsService(MeterRegistry registry) {
        this.restaurantCreatedCounter = Counter.builder("app_restaurants_created_total")
                .description("Total number of restaurants created")
                .tag("type", "business").register(registry);
        this.operationFailedCounter = Counter.builder("app_errors_total")
                .description("Total number of failed operations")
                .tag("type", "error").register(registry);
        this.restaurantLookupTimer = Timer.builder("app_restaurant_lookup_duration_seconds")
                .description("Time taken to look up a restaurant")
                .tag("type", "performance").register(registry);
        Gauge.builder("app_active_restaurants", activeRestaurants, AtomicInteger::get)
                .description("Number of currently active restaurants")
                .tag("type", "resource").register(registry);
        this.restaurantDeletedCounter = Counter.builder("app_restaurants_deleted_total")
                .description("Total number of restaurants deleted")
                .tag("type", "domain").register(registry);
    }

    public void recordRestaurantCreated() {
        restaurantCreatedCounter.increment();
        activeRestaurants.incrementAndGet();
    }

    public void recordRestaurantDeleted() {
        restaurantDeletedCounter.increment();
        activeRestaurants.decrementAndGet();
    }

    public void recordOperationFailed() {
        operationFailedCounter.increment();
    }

    public Timer getRestaurantLookupTimer() {
        return restaurantLookupTimer;
    }
}
