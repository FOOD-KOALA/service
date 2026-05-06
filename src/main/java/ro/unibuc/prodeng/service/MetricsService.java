package ro.unibuc.prodeng.service;

import io.micrometer.core.instrument.*;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

@Service
public class MetricsService {

    // 1. BUSINESS — comenzi create
    private final Counter ordersCreatedCounter;

    // 2. PERFORMANCE — timp de raspuns la getOrderById
    private final Timer orderFetchTimer;

    // 3. ERROR — erori NOT_FOUND la comenzi
    private final Counter orderNotFoundCounter;

    // 4. RESOURCE — utilizatori creati total (Gauge)
    private final AtomicInteger totalUsers = new AtomicInteger(0);

    // 5. DOMAIN-SPECIFIC — distributie numar iteme per comanda
    private final DistributionSummary orderItemsSummary;

    public MetricsService(MeterRegistry registry) {

        this.ordersCreatedCounter = Counter.builder("foodkoala.orders.created.total")
                .description("Numarul total de comenzi create")
                .register(registry);

        this.orderFetchTimer = Timer.builder("foodkoala.order.fetch.duration")
                .description("Durata aducerii unei comenzi dupa ID")
                .register(registry);

        this.orderNotFoundCounter = Counter.builder("foodkoala.orders.notfound.total")
                .description("Numarul de cereri pentru comenzi inexistente")
                .register(registry);

        Gauge.builder("foodkoala.users.total", totalUsers, AtomicInteger::get)
                .description("Numarul curent de utilizatori in sistem")
                .register(registry);

        this.orderItemsSummary = DistributionSummary.builder("foodkoala.order.items.count")
                .description("Distributia numarului de iteme per comanda")
                .register(registry);
    }

    public void recordOrderCreated(int itemCount) {
        ordersCreatedCounter.increment();
        orderItemsSummary.record(itemCount);
    }

    public <T> T recordOrderFetch(Supplier<T> supplier) {
        return orderFetchTimer.record(supplier);
    }

    public void recordOrderNotFound() {
        orderNotFoundCounter.increment();
    }

    public void incrementUserCount() {
        totalUsers.incrementAndGet();
    }

    public void decrementUserCount() {
        totalUsers.decrementAndGet();
    }
}