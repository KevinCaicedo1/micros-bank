package com.bank.movement.application.output.port;

import java.time.LocalDate;
import java.util.UUID;

import com.bank.movement.infrastructure.output.repository.entity.MovementEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RepositoryMovementPort {
    Mono<Void> save(MovementEntity movementDom);
    Mono<MovementEntity> findByMovementId(UUID movementId);
    Flux<MovementEntity> findAll();
    Flux<MovementEntity> findByAccountId(UUID accountId);
    Mono<Void> deleteByMovementId(UUID movementId);
    Flux<MovementEntity> findByClientIdAndDate(UUID clientId, LocalDate startDate, LocalDate endDate);// Update the account ID in the movement entity
}
