package com.bank.movement.infrastructure.output.adapter.request;

import com.bank.movement.application.input.port.CustomerServiceClient;
import com.bank.movement.domain.RSCustomerDom;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerServiceClientImpl implements CustomerServiceClient {

    private final WebClient.Builder webClientBuilder;
    @Value("${custom.service-url}")
    private String customerServiceUrl;

    @Override
    public Mono<RSCustomerDom> getCustomerById(UUID customerId) {
        log.info("Fetching customer information for customerId: {}", customerId);
        return webClientBuilder.build()
                .get()
                .uri(customerServiceUrl + "/{customerId}", customerId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> {
                    log.error("Client error while fetching customer information: {}", clientResponse.statusCode());
                    return Mono.error(new RuntimeException("Client error while fetching customer information"));
                })
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> {
                    log.error("Server error while fetching customer information: {}", clientResponse.statusCode());
                    return Mono.error(new RuntimeException("Server error while fetching customer information"));
                })
                .bodyToMono(RSCustomerDom.class)
                .doOnError(error -> log.error("Error fetching customer information: {}", error.getMessage()));
    }
}