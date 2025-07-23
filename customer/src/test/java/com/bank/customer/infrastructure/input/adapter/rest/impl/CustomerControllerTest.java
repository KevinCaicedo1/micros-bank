package com.bank.customer.infrastructure.input.adapter.rest.impl;

import com.bank.customer.CustomerApplication;
import com.bank.customer.infrastructure.output.repository.CustomerJPARepository;
import com.bank.customer.infrastructure.output.repository.entity.CustomerEntity;
import com.bank.customer.utils.MockCustomerDataUtils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(
        classes = CustomerApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
class CustomerControllerTest {

    private static final String BASE_URL = "/api/v1/customers";
    private static final String CUSTOMER_ID = MockCustomerDataUtils.CUSTOMER_ID;

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private CustomerJPARepository customerJPARepository;

    private CustomerEntity customerEntity;

    @BeforeEach
    void setUp() {
        customerEntity = MockCustomerDataUtils.getCustomerEntity();
    }

    @Test
    @DisplayName("shouldReturnCustomerByIdSuccessfully_endToEnd")
    void shouldReturnCustomerByIdSuccessfully_endToEnd() {
        // Given
        when(customerJPARepository.findByCustomerId(UUID.fromString(CUSTOMER_ID)))
                .thenReturn(customerEntity);

        // When & Then
        webTestClient.get()
                .uri(BASE_URL + "/" + CUSTOMER_ID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.customerId").isEqualTo(CUSTOMER_ID)
                .jsonPath("$.name").isEqualTo(MockCustomerDataUtils.NAME)
                .jsonPath("$.identification").isEqualTo(MockCustomerDataUtils.IDENTIFICATION)
                .jsonPath("$.isActive").isEqualTo(MockCustomerDataUtils.IS_ACTIVE);
    }

    @Test
    @DisplayName("shouldReturnNotFoundWhenCustomerDoesNotExist_endToEnd")
    void shouldReturnNotFoundWhenCustomerDoesNotExist_endToEnd() {
        // Given
        when(customerJPARepository.findByCustomerId(any(UUID.class)))
                .thenReturn(null);

        // When & Then
        webTestClient.get()
                .uri(BASE_URL + "/" + CUSTOMER_ID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.status").isEqualTo(HttpStatus.NOT_FOUND.value())
                .jsonPath("$.message").exists();
    }

    @Test
    @DisplayName("shouldCreateCustomerSuccessfully_endToEnd")
    void shouldCreateCustomerSuccessfully_endToEnd() {
        // Given
        when(customerJPARepository.save(any(CustomerEntity.class)))
                .thenReturn(customerEntity);

        // When & Then
        webTestClient.post()
                .uri(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(MockCustomerDataUtils.getCustomerDom())
                .exchange()
                .expectStatus().is2xxSuccessful();
    }

    @Test
    @DisplayName("shouldDeleteCustomerSuccessfully_endToEnd")
    void shouldDeleteCustomerSuccessfully_endToEnd() {
        // Given
        when(customerJPARepository.findByCustomerId(UUID.fromString(CUSTOMER_ID)))
                .thenReturn(customerEntity);

        // When & Then
        webTestClient.delete()
                .uri(BASE_URL + "/" + CUSTOMER_ID)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    @DisplayName("shouldReturnNotFoundWhenDeleteCustomerDoesNotExist_endToEnd")
    void shouldReturnNotFoundWhenDeleteCustomerDoesNotExist_endToEnd() {
        // Given
        when(customerJPARepository.findByCustomerId(any(UUID.class)))
                .thenReturn(null);

        // When & Then
        webTestClient.delete()
                .uri(BASE_URL + "/" + CUSTOMER_ID)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.status").isEqualTo(HttpStatus.NOT_FOUND.value())
                .jsonPath("$.message").exists();
    }
}
