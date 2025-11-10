package com.bank.movement.application.output.port;

import java.time.LocalDate;
import java.util.UUID;

import com.bank.movement.domain.RSMovementDom;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RepositoryMovementPort {
    Mono<Void> save(RSMovementDom movementDom);
    Mono<RSMovementDom> findByMovementId(UUID movementId);
    Flux<RSMovementDom> findAll();
    Flux<RSMovementDom> findByAccountId(UUID accountId);
    Mono<Void> deleteByMovementId(UUID movementId);
    Flux<RSMovementDom> findByClientIdAndDate(UUID clientId, LocalDate startDate, LocalDate endDate);// Update the account ID in the movement entity
}
