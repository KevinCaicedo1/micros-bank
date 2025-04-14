package com.bank.customer.application.output.port;

import java.util.UUID;

import com.bank.customer.domain.CustomerDom;
import com.bank.customer.infrastructure.output.repository.entity.CustomerEntity;

import reactor.core.publisher.Mono;

public interface RepositoryPort {
    Mono<Void> save(CustomerDom customerDom);
    Mono<CustomerEntity> findByCustomerId(UUID customerId);
    Mono<CustomerEntity> findByIdentification(String identification);
    Mono<Void> deleteByCustomerId(UUID customerId);
    Mono<Void> update(UUID customerId, CustomerDom customerDom);
}
