package com.bank.customer.application.output.port;

import java.util.UUID;

import com.bank.customer.domain.CustomerDom;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerRepositoryPort {
    Mono<Void> save(CustomerDom customerDom);
    Mono<CustomerDom> findByCustomerId(UUID customerId);
    Mono<CustomerDom> findByIdentification(String identification);
    Mono<Void> deleteByCustomerId(UUID customerId);
    Mono<Void> update(UUID customerId, CustomerDom customerDom);
    Flux<CustomerDom> findAllCustomers();
}
