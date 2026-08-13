package com.example.nestedvalidation.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateOrderRequest(
    @NotBlank String customerId,
    @NotNull ShippingAddress shippingAddress
) {

    public record ShippingAddress(
        @NotBlank String postalCode,
        @NotBlank String city
    ) {
    }
}
