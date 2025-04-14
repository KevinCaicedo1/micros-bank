package com.bank.customer.infrastructure.output.repository.impl;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.reactive.TransactionalOperator;

import com.bank.customer.application.service.CustomerTransactionalService;
import com.bank.customer.domain.enums.ErrorMessage;
import com.bank.customer.infrastructure.exception.CustomerException;
import com.bank.customer.infrastructure.output.repository.CustomerJPARepository;
import com.bank.customer.infrastructure.output.repository.CustomerRepository;
import com.bank.customer.infrastructure.output.repository.entity.CustomerEntity;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Repository
@RequiredArgsConstructor
@Slf4j
public class CustomerRepositoryImpl implements CustomerRepository {

    private final CustomerJPARepository customerJPARepository;
    private final CustomerTransactionalService customerTransactionalService;

    @Override
    public Mono<Void> save(CustomerEntity customerEntity) {
        return Mono.just(customerJPARepository.save(customerEntity)).then()
                .doOnSuccess(aVoid -> log.info("Customer saved successfully: {}", customerEntity))
                .doOnError(error -> log.error("Error saving customer: {}", error.getMessage()));
    }

    @Override
    public Mono<CustomerEntity> findByCustomerId(UUID customerId) {
        log.info("Finding customer by ID: {}", customerId);
        CustomerEntity customerEntity = customerJPARepository.findByCustomerId(customerId);
        if (customerEntity == null) {
            log.error("Customer not found with ID: {}", customerId);
            return Mono.error(new CustomerException(ErrorMessage.CUSTOMER_NOT_FOUND.message(), HttpStatus.NOT_FOUND));
        }

        return Mono.just(customerEntity)
                .doOnNext(customer -> log.info("Customer found: {}", customer))
                .doOnError(error -> log.error("Error finding customer by ID: {}", error.getMessage()));
    }

    @Override
    public Mono<CustomerEntity> findByIdentification(String identification) {
        log.info("Finding customer by identification: {}", identification);
        CustomerEntity customerEntity = customerJPARepository.findByIdentification(identification);
        if (customerEntity == null) {
            log.error("Customer not found with identification: {}", identification);
            return Mono.error(new CustomerException(ErrorMessage.CUSTOMER_NOT_FOUND.message(), HttpStatus.NOT_FOUND));
        }

        return Mono.just(customerEntity)
                .doOnNext(customer -> log.info("Customer found by identification: {}", customer))
                .doOnError(error -> log.error("Error finding customer by identification: {}", error.getMessage()));
    }

    @Override
    public Mono<Void> deleteByCustomerId(UUID customerId) {
        log.info("Deleting customer by ID: {}", customerId);
        CustomerEntity customerEntity = customerJPARepository.findByCustomerId(customerId);
        if (customerEntity == null) {
            log.error("Customer not found with ID: {}", customerId);
            return Mono.error(new CustomerException(ErrorMessage.CUSTOMER_NOT_FOUND.message(), HttpStatus.NOT_FOUND));
        }
        return Mono.fromRunnable(() -> customerTransactionalService.deleteCustomer(customerId))
                .subscribeOn(Schedulers.boundedElastic())
                .doOnSuccess(aVoid -> log.info("Customer deleted successfully with ID: {}", customerId))
                .doOnError(error -> log.error("Error deleting customer: {}", error.getMessage()))
                .then();

    }

    @Override
    public Mono<Void> update(CustomerEntity customerEntity) {
        log.info("Updating customer: {}", customerEntity);
        CustomerEntity existingCustomer = customerJPARepository.findByCustomerId(customerEntity.getCustomerId());
        if (existingCustomer == null) {
            log.error("Customer not found with ID: {}", customerEntity.getCustomerId());
            return Mono.error(new CustomerException(ErrorMessage.CUSTOMER_NOT_FOUND.message(), HttpStatus.NOT_FOUND));
        }
        return Mono.just(customerEntity)
                .map(customer -> customerJPARepository.save(customer))
                .doOnSuccess(aVoid -> log.info("Customer updated successfully: {}", customerEntity))
                .doOnError(error -> log.error("Error updating customer: {}", error.getMessage()))
                .then();
    }

}
