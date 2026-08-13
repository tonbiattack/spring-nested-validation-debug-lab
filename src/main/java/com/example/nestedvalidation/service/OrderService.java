package com.example.nestedvalidation.service;

import com.example.nestedvalidation.api.CreateOrderRequest;
import com.example.nestedvalidation.api.OrderResponse;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private final AtomicInteger sequence = new AtomicInteger();
    private final ConcurrentMap<String, OrderResponse> orders = new ConcurrentHashMap<>();

    public OrderResponse create(CreateOrderRequest request) {
        String orderId = "order-" + sequence.incrementAndGet();
        OrderResponse response = new OrderResponse(
            orderId,
            request.customerId(),
            request.shippingAddress().postalCode()
        );
        orders.put(orderId, response);
        logger.info("Created orderId={}, customerId={}, postalCode={}", orderId, request.customerId(), request.shippingAddress().postalCode());
        return response;
    }

    public int count() {
        return orders.size();
    }

    public Collection<OrderResponse> findAll() {
        return orders.values();
    }

    public void clear() {
        orders.clear();
        sequence.set(0);
    }
}
