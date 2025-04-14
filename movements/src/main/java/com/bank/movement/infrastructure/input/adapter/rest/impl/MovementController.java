package com.bank.movement.infrastructure.input.adapter.rest.impl;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

import com.bank.movement.application.input.port.MovementService;
import com.bank.movement.infrastructure.input.adapter.rest.MovementsApi;
import com.bank.movement.infrastructure.input.adapter.rest.mapper.MovementMapper;
import com.bank.movement.infrastructure.input.adapter.rest.model.RQCreateMovement;
import com.bank.movement.infrastructure.input.adapter.rest.model.RSMovement;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@AllArgsConstructor
@Slf4j
@Validated
@RequestMapping("/api/v1")
public class MovementController implements MovementsApi {

    private final MovementService movementService;
    private final MovementMapper movementMapper;

    @Override
    public Mono<ResponseEntity<Flux<RSMovement>>> movementsAccountAccountNumberGet(String accountNumber,
            ServerWebExchange exchange) {
        log.info("Fetching movements for account number: {}", accountNumber);
        return movementService.getMovementsByAccount(accountNumber)
                .map(movementMapper::toMovement)
                .collectList()
                .map(movements -> ResponseEntity.ok(Flux.fromIterable(movements)))
                .doOnError(error -> log.error("Error retrieving movements for account {}: {}", accountNumber,
                        error.getMessage()))
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build()));
    }

    @Override
    public Mono<ResponseEntity<Flux<RSMovement>>> movementsGet(ServerWebExchange exchange) {
        log.info("Fetching all movements");
        return movementService.getAllMovements()
                .map(movementMapper::toMovement)
                .collectList()
                .map(movements -> ResponseEntity.ok(Flux.fromIterable(movements)))
                .doOnError(error -> log.error("Error retrieving all movements: {}", error.getMessage()))
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()));
    }

    @Override
    public Mono<ResponseEntity<Void>> movementsPost(@Valid Mono<RQCreateMovement> rqCreateMovement, ServerWebExchange exchange) {
        log.info("Creating movement");
        return rqCreateMovement
                .map(movementMapper::toDom)
                .flatMap(movementService::createMovement)
                .then(Mono.just(ResponseEntity.status(HttpStatus.CREATED).build()));
    }
}
