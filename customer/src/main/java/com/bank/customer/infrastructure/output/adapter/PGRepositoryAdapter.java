package com.bank.customer.infrastructure.output.adapter;

import java.util.UUID;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.bank.customer.application.output.port.RepositoryPort;
import com.bank.customer.domain.CustomerDom;
import com.bank.customer.infrastructure.output.adapter.mapper.PGRepositoryMapper;
import com.bank.customer.infrastructure.output.repository.CustomerRepository;
import com.bank.customer.infrastructure.output.repository.entity.CustomerEntity;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Primary
@Service
@RequiredArgsConstructor
@Slf4j
public class PGRepositoryAdapter implements RepositoryPort {

    private final CustomerRepository customerRepository;
    private final PGRepositoryMapper pgRepositoryMapper;

    @Override
    public Mono<Void> save(CustomerDom customerDom) {
        log.info("Saving customer: {}", customerDom);
        CustomerEntity customerEntity = pgRepositoryMapper.toEntity(customerDom);
        customerEntity.setCustomerId(UUID.randomUUID()); // Generate a new UUID for the customer
        return customerRepository.save(customerEntity)
                .doOnSuccess(aVoid -> log.info("Customer saved successfully"))
                .doOnError(error -> log.error("Error saving customer: {}", error.getMessage()));
    }

    @Override
    public Mono<CustomerEntity> findByCustomerId(UUID customerId) {
        log.info("Finding customer by ID: {}", customerId);
        return customerRepository.findByCustomerId(customerId)
                .doOnNext(customer -> log.info("Customer found: {}", customer))
                .doOnError(error -> log.error("Error finding customer by ID: {}", error.getMessage()));
    }

    @Override
    public Mono<CustomerEntity> findByIdentification(String identification) {
        log.info("Finding customer by identification: {}", identification);
        return customerRepository.findByIdentification(identification)
                .doOnNext(customer -> log.info("Customer found by identification: {}", customer))
                .doOnError(error -> log.error("Error finding customer by identification: {}", error.getMessage()));
    }

    @Override
    public Mono<Void> deleteByCustomerId(UUID customerId) {
        log.info("Deleting customer by ID: {}", customerId);
        return customerRepository.deleteByCustomerId(customerId)
                .doOnSuccess(aVoid -> log.info("Customer deleted successfully"))
                .doOnError(error -> log.error("Error deleting customer: {}", error.getMessage()));
    }

    @Override
    public Mono<Void> update(UUID customerId, CustomerDom customerDom) {
        log.info("Updating customer: {}", customerDom);
        
        CustomerEntity customerEntity = pgRepositoryMapper.toEntity(customerDom);
        customerEntity.setCustomerId(customerId); // Set the customer ID for the update
        return customerRepository.findByCustomerId(customerId)
                .flatMap(existingCustomer -> {
                    customerEntity.setId(existingCustomer.getId()); // Set the existing ID to update
                    return customerRepository.update(customerEntity);
                })
                .doOnNext(updatedCustomer -> log.info("Customer updated successfully: {}", updatedCustomer))
                .doOnError(error -> log.error("Error updating customer: {}", error.getMessage()));
    }

}

