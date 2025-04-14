package com.bank.movement.application.input.port;

import java.util.UUID;

import com.bank.movement.domain.RSCustomerDom;
import reactor.core.publisher.Mono;

public interface CustomerServiceClient {
    Mono<RSCustomerDom> getCustomerById(UUID customerId);
}