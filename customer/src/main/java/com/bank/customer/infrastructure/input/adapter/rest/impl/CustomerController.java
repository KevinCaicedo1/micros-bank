package com.bank.customer.infrastructure.input.adapter.rest.impl;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
// importar de build CustomersApi
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

import com.bank.customer.application.input.port.CustomerService;
import com.bank.customer.infrastructure.input.adapter.rest.ApiApi;
import com.bank.customer.infrastructure.input.adapter.rest.mapper.CustomerMapper;
import com.bank.customer.infrastructure.input.adapter.rest.model.CustomerResponse;
import com.bank.customer.infrastructure.input.adapter.rest.model.RQCreateCustomer;
import com.bank.customer.infrastructure.input.adapter.rest.model.RQUpdateCustomer;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RestController
@AllArgsConstructor
@Slf4j
@Validated
public class CustomerController implements ApiApi {

    private final CustomerService customerService;
    private final CustomerMapper customerMapper;
    @Override
    public Mono<ResponseEntity<Void>> createCustomer(@Valid Mono<RQCreateCustomer> rqCreateCustomer,
            ServerWebExchange exchange) {
                log.info("Creating customer: {}", rqCreateCustomer);
                return rqCreateCustomer
                        .map(customerMapper::fromRQCreateCustomer)
                        .flatMap(customerService::createCustomer)
                        .map(ResponseEntity.status(HttpStatus.CREATED)::body)
                        .doOnError(error -> log.error("Error creating customer: {}", error.getMessage()));
    }

    @Override
    public Mono<ResponseEntity<Void>> deleteCustomer(UUID customerId, ServerWebExchange exchange) {
        log.info("Deleting customer with ID: {}", customerId);
        return customerService.deleteCustomer(customerId)
                .doOnSuccess(aVoid -> log.info("Customer deleted successfully"))
                .doOnError(error -> log.error("Error deleting customer: {}", error.getMessage()))
                .then(Mono.just(ResponseEntity.status(HttpStatus.NO_CONTENT).build()));
    }

    @Override
    public Mono<ResponseEntity<CustomerResponse>> getCustomerById(UUID customerId, ServerWebExchange exchange) {
        log.info("Fetching customer by ID: {}", customerId);
        return customerService.getCustomerById(customerId)
                .map(customerMapper::toCustomerResponse)
                .doOnNext(customer -> log.info("Customer found: {}", customer))
                .doOnError(error -> log.error("Error fetching customer by ID: {}", error.getMessage()))
                .map(ResponseEntity.ok()::body);
    }

    @Override
    public Mono<ResponseEntity<Void>> updateCustomer(UUID customerId,
            @Valid Mono<RQUpdateCustomer> rqUpdateCustomer, ServerWebExchange exchange) {
        log.info("Updating customer with ID: {}", customerId);
        return rqUpdateCustomer
                .map(customerMapper::fromRQUpdateCustomer)
                .flatMap(customer -> customerService.updateCustomer(customerId, customer))
                .doOnSuccess(aVoid -> log.info("Customer updated successfully"))
                .doOnError(error -> log.error("Error updating customer: {}", error.getMessage()))
                .then(Mono.just(ResponseEntity.status(HttpStatus.NO_CONTENT).build()));
    }


}