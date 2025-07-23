package com.bank.customer.application.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.bank.customer.application.output.port.RepositoryPort;
import com.bank.customer.application.service.CustomerServiceImpl;
import com.bank.customer.domain.CustomerDom; 
import com.bank.customer.infrastructure.output.adapter.mapper.PGRepositoryMapper; 
import com.bank.customer.infrastructure.output.repository.entity.CustomerEntity;
import com.bank.customer.utils.MockDataUtils;

import org.junit.jupiter.api.BeforeEach;  
import org.junit.jupiter.api.DisplayName;  
import org.mockito.MockitoAnnotations; 
import reactor.core.publisher.Flux; 
import reactor.core.publisher.Mono; 
import reactor.test.StepVerifier;

import java.util.UUID;

class CustomerServiceImplTest {

private static final String INVALID_CUSTOMER_ID = "00000000-0000-0000-0000-000000000000";
private static final String NOT_FOUND_MESSAGE = "Customer not found";

@Mock
private RepositoryPort repositoryPort;

@Mock
private PGRepositoryMapper pgRepositoryMapper;

@InjectMocks
private CustomerServiceImpl customerServiceImpl;

@BeforeEach
void setUp() {
    MockitoAnnotations.openMocks(this);
}

@Test
@DisplayName("shouldCreateCustomerSuccessfullyWhenInputIsValid")
void shouldCreateCustomerSuccessfullyWhenInputIsValid() {
    CustomerDom customerDom = MockDataUtils.getCustomerDom();
    when(repositoryPort.save(customerDom)).thenReturn(Mono.empty());

    StepVerifier.create(customerServiceImpl.createCustomer(customerDom))
            .verifyComplete();

    verify(repositoryPort).save(customerDom);
}

@Test
@DisplayName("shouldReturnErrorWhenCreateCustomerFails")
void shouldReturnErrorWhenCreateCustomerFails() {
    CustomerDom customerDom = MockDataUtils.getCustomerDom();
    when(repositoryPort.save(customerDom)).thenReturn(Mono.error(new RuntimeException("DB error")));

    StepVerifier.create(customerServiceImpl.createCustomer(customerDom))
            .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                    throwable.getMessage().equals("DB error"))
            .verify();

    verify(repositoryPort).save(customerDom);
}

@Test
@DisplayName("shouldReturnCustomerWhenGetCustomerByIdIsSuccessful")
void shouldReturnCustomerWhenGetCustomerByIdIsSuccessful() {
    UUID customerId = UUID.fromString(MockDataUtils.CUSTOMER_ID);
    CustomerEntity entity = MockDataUtils.getCustomerEntity();
    CustomerDom dom = MockDataUtils.getCustomerDom();

    when(repositoryPort.findByCustomerId(customerId)).thenReturn(Mono.just(entity));
    when(pgRepositoryMapper.toDomain(entity)).thenReturn(dom);

    StepVerifier.create(customerServiceImpl.getCustomerById(customerId))
            .expectNext(dom)
            .verifyComplete();

    verify(repositoryPort).findByCustomerId(customerId);
    verify(pgRepositoryMapper).toDomain(entity);
}

@Test
@DisplayName("shouldReturnEmptyWhenCustomerByIdNotFound")
void shouldReturnEmptyWhenCustomerByIdNotFound() {
    UUID customerId = UUID.fromString(INVALID_CUSTOMER_ID);

    when(repositoryPort.findByCustomerId(customerId)).thenReturn(Mono.empty());

    StepVerifier.create(customerServiceImpl.getCustomerById(customerId))
            .verifyComplete();

    verify(repositoryPort).findByCustomerId(customerId);
}

@Test
@DisplayName("shouldReturnCustomerWhenGetCustomerByIdentificationIsSuccessful")
void shouldReturnCustomerWhenGetCustomerByIdentificationIsSuccessful() {
    String identification = MockDataUtils.IDENTIFICATION;
    CustomerEntity entity = MockDataUtils.getCustomerEntity();
    CustomerDom dom = MockDataUtils.getCustomerDom();

    when(repositoryPort.findByIdentification(identification)).thenReturn(Mono.just(entity));
    when(pgRepositoryMapper.toDomain(entity)).thenReturn(dom);

    StepVerifier.create(customerServiceImpl.getCustomerByIdentification(identification))
            .expectNext(dom)
            .verifyComplete();

    verify(repositoryPort).findByIdentification(identification);
    verify(pgRepositoryMapper).toDomain(entity);
}

@Test
@DisplayName("shouldReturnEmptyWhenCustomerByIdentificationNotFound")
void shouldReturnEmptyWhenCustomerByIdentificationNotFound() {
    String identification = "not_found";

    when(repositoryPort.findByIdentification(identification)).thenReturn(Mono.empty());

    StepVerifier.create(customerServiceImpl.getCustomerByIdentification(identification))
            .verifyComplete();

    verify(repositoryPort).findByIdentification(identification);
}

@Test
@DisplayName("shouldUpdateCustomerSuccessfullyWhenInputIsValid")
void shouldUpdateCustomerSuccessfullyWhenInputIsValid() {
    UUID customerId = UUID.fromString(MockDataUtils.CUSTOMER_ID);
    CustomerDom customerDom = MockDataUtils.getCustomerDom();

    when(repositoryPort.update(customerId, customerDom)).thenReturn(Mono.empty());

    StepVerifier.create(customerServiceImpl.updateCustomer(customerId, customerDom))
            .verifyComplete();

    verify(repositoryPort).update(customerId, customerDom);
}

@Test
@DisplayName("shouldReturnErrorWhenUpdateCustomerFails")
void shouldReturnErrorWhenUpdateCustomerFails() {
    UUID customerId = UUID.fromString(MockDataUtils.CUSTOMER_ID);
    CustomerDom customerDom = MockDataUtils.getCustomerDom();

    when(repositoryPort.update(customerId, customerDom)).thenReturn(Mono.error(new RuntimeException("Update failed")));

    StepVerifier.create(customerServiceImpl.updateCustomer(customerId, customerDom))
            .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                    throwable.getMessage().equals("Update failed"))
            .verify();

    verify(repositoryPort).update(customerId, customerDom);
}

@Test
@DisplayName("shouldDeleteCustomerSuccessfullyWhenInputIsValid")
void shouldDeleteCustomerSuccessfullyWhenInputIsValid() {
    UUID customerId = UUID.fromString(MockDataUtils.CUSTOMER_ID);

    when(repositoryPort.deleteByCustomerId(customerId)).thenReturn(Mono.empty());

    StepVerifier.create(customerServiceImpl.deleteCustomer(customerId))
            .verifyComplete();

    verify(repositoryPort).deleteByCustomerId(customerId);
}

@Test
@DisplayName("shouldReturnErrorWhenDeleteCustomerFails")
void shouldReturnErrorWhenDeleteCustomerFails() {
    UUID customerId = UUID.fromString(MockDataUtils.CUSTOMER_ID);

    when(repositoryPort.deleteByCustomerId(customerId)).thenReturn(Mono.error(new RuntimeException("Delete failed")));

    StepVerifier.create(customerServiceImpl.deleteCustomer(customerId))
            .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                    throwable.getMessage().equals("Delete failed"))
            .verify();

    verify(repositoryPort).deleteByCustomerId(customerId);
}

@Test
@DisplayName("shouldDeleteCustomerByIdentificationSuccessfully")
void shouldDeleteCustomerByIdentificationSuccessfully() {
    String identification = MockDataUtils.IDENTIFICATION;
    CustomerEntity entity = MockDataUtils.getCustomerEntity();

    when(repositoryPort.findByIdentification(identification)).thenReturn(Mono.just(entity));
    when(repositoryPort.deleteByCustomerId(entity.getCustomerId())).thenReturn(Mono.empty());

    StepVerifier.create(customerServiceImpl.deleteCustomerByIdentification(identification))
            .verifyComplete();

    verify(repositoryPort).findByIdentification(identification);
    verify(repositoryPort).deleteByCustomerId(entity.getCustomerId());
}

@Test
@DisplayName("shouldReturnErrorWhenDeleteCustomerByIdentificationFails")
void shouldReturnErrorWhenDeleteCustomerByIdentificationFails() {
    String identification = MockDataUtils.IDENTIFICATION;
    CustomerEntity entity = MockDataUtils.getCustomerEntity();

    when(repositoryPort.findByIdentification(identification)).thenReturn(Mono.just(entity));
    when(repositoryPort.deleteByCustomerId(entity.getCustomerId())).thenReturn(Mono.error(new RuntimeException("Delete failed")));

    StepVerifier.create(customerServiceImpl.deleteCustomerByIdentification(identification))
            .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                    throwable.getMessage().equals("Delete failed"))
            .verify();

    verify(repositoryPort).findByIdentification(identification);
    verify(repositoryPort).deleteByCustomerId(entity.getCustomerId());
}

@Test
@DisplayName("shouldReturnAllCustomersSuccessfully")
void shouldReturnAllCustomersSuccessfully() {
    CustomerEntity entity = MockDataUtils.getCustomerEntity();
    CustomerDom dom = MockDataUtils.getCustomerDom();

    when(repositoryPort.findAllCustomers()).thenReturn(Flux.just(entity));
    when(pgRepositoryMapper.toDomain(entity)).thenReturn(dom);

    StepVerifier.create(customerServiceImpl.getAllCustomers())
            .expectNext(dom)
            .verifyComplete();

    verify(repositoryPort).findAllCustomers();
    verify(pgRepositoryMapper).toDomain(entity);
}

@Test
@DisplayName("shouldReturnEmptyWhenNoCustomersExist")
void shouldReturnEmptyWhenNoCustomersExist() {
    when(repositoryPort.findAllCustomers()).thenReturn(Flux.empty());

    StepVerifier.create(customerServiceImpl.getAllCustomers())
            .verifyComplete();

    verify(repositoryPort).findAllCustomers();
}
}