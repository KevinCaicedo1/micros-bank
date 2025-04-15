package com.bank.customer.application.input.port;

import java.util.UUID;

import com.bank.customer.domain.CustomerDom;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerService {

    Mono<Void> createCustomer(CustomerDom customerDom);
    Mono<CustomerDom> getCustomerById(UUID customerId);
    Mono<CustomerDom> getCustomerByIdentification(String identification);
    Mono<Void> updateCustomer(UUID customerId, CustomerDom customerDom);
    Mono<Void> deleteCustomer(UUID customerId);
    Mono<Void> deleteCustomerByIdentification(String identification);
    Flux<CustomerDom> getAllCustomers();
    
} 