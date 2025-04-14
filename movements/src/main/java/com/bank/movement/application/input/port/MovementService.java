package com.bank.movement.application.input.port;

import java.util.UUID;

import com.bank.movement.domain.RQCreateMovementDom;
import com.bank.movement.domain.RSMovementDom;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MovementService {

    Mono<Void> createMovement(RQCreateMovementDom movementDom);
    Flux<RSMovementDom> getAllMovements();
    Mono<RSMovementDom> getMovementById(UUID movementId);
    Flux<RSMovementDom> getMovementsByAccount(String accountNumber);
    Mono<Void> deleteMovementById(UUID movementId);
} 