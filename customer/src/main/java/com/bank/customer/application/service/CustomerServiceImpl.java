package com.bank.customer.application.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.bank.customer.application.input.port.CustomerService;
import com.bank.customer.application.output.port.RepositoryPort;
import com.bank.customer.domain.CustomerDom;
import com.bank.customer.infrastructure.output.adapter.mapper.PGRepositoryMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final RepositoryPort repositoryPort;
    private final PGRepositoryMapper pgRepositoryMapper;

    @Override
    public Mono<Void> createCustomer(CustomerDom customerDom) {
        log.info("Creating customer: {}", customerDom);
        return repositoryPort.save(customerDom)
                .doOnSuccess(aVoid -> log.info("Customer created successfully"))
                .doOnError(error -> log.error("Error creating customer: {}", error.getMessage()));

    }

    @Override
    public Mono<CustomerDom> getCustomerById(UUID customerId) {
        log.info("Fetching customer by ID: {}", customerId);
        return repositoryPort.findByCustomerId(customerId)
                .map(pgRepositoryMapper::toDomain)
                .doOnNext(customer -> log.info("Customer found: {}", customer))
                .doOnError(error -> log.error("Error fetching customer by ID: {}", error.getMessage()));

    }

    @Override
    public Mono<CustomerDom> getCustomerByIdentification(String identification) {
        log.info("Fetching customer by identification: {}", identification);
        return repositoryPort.findByIdentification(identification)
                .map(pgRepositoryMapper::toDomain)
                .doOnNext(customer -> log.info("Customer found by identification: {}", customer))
                .doOnError(error -> log.error("Error fetching customer by identification: {}", error.getMessage()));
    }

    @Override
    public Mono<Void> updateCustomer(UUID customerId, CustomerDom customerDom) {
        log.info("Updating customer: {}", customerDom);
        return repositoryPort.update(customerId, customerDom)
                .doOnSuccess(aVoid -> log.info("Customer updated successfully"))
                .doOnError(error -> log.error("Error updating customer: {}", error.getMessage()));
    }

    @Override
    public Mono<Void> deleteCustomer(UUID customerId) {
        log.info("Deleting customer by ID: {}", customerId);
        return repositoryPort.deleteByCustomerId(customerId)
                .doOnSuccess(aVoid -> log.info("Customer deleted successfully"))
                .doOnError(error -> log.error("Error deleting customer: {}", error.getMessage()));
    }

    @Override
    public Mono<Void> deleteCustomerByIdentification(String identification) {
        log.info("Deleting customer by identification: {}", identification);
        return repositoryPort.findByIdentification(identification)
                .flatMap(customer -> repositoryPort.deleteByCustomerId(customer.getCustomerId()))
                .doOnSuccess(aVoid -> log.info("Customer deleted successfully"))
                .doOnError(error -> log.error("Error deleting customer by identification: {}", error.getMessage()));
    }

    
}
    