package com.bank.customer.infrastructure.output.repository;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.bank.customer.infrastructure.output.repository.entity.CustomerEntity;

import reactor.core.publisher.Mono;

@Repository
public interface CustomerRepository {

    Mono<Void> save(CustomerEntity customerEntity); // Save a customer entity
    Mono<CustomerEntity> findByCustomerId(UUID customerId); // Find a customer by ID
    Mono<CustomerEntity> findByIdentification(String identification); // Find a customer by identification
    Mono<Void> deleteByCustomerId(UUID customerId); // Delete a customer by ID
    Mono<Void> update(CustomerEntity customerEntity); // Update a customer entity

    
} 
