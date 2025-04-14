package com.bank.movement.infrastructure.output.repository;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.bank.movement.infrastructure.output.repository.entity.AccountEntity;
import com.bank.movement.infrastructure.output.repository.entity.MovementEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface MovementRepository {

    Mono<Void> createMovement(MovementEntity movementEntity);
    Mono<MovementEntity> getMovementById(UUID movementId);
    Flux<MovementEntity> getAllMovements();
    Flux<MovementEntity> getMovementsByAccount(AccountEntity accountEntity);
    Flux<MovementEntity> getMovementsByClientIdAndDate(UUID clientId, LocalDate startDate, LocalDate endDate);
    Mono<Void> deleteMovementById(UUID movementId);

} 
