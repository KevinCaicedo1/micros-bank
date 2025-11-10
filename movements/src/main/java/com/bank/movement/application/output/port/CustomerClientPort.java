package com.bank.movement.application.output.port;

import java.util.UUID;

import com.bank.movement.domain.RSCustomerDom;
import reactor.core.publisher.Mono;

public interface CustomerClientPort {
    Mono<RSCustomerDom> getCustomerById(UUID customerId);
}
