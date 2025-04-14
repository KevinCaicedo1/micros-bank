package com.bank.movement.infrastructure.output.repository.impl;

import java.sql.Date;
import java.time.LocalDate;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import com.bank.movement.domain.enums.ErrorMessage;
import com.bank.movement.infrastructure.exception.CustomException;
import com.bank.movement.infrastructure.output.repository.MovementJPARepository;
import com.bank.movement.infrastructure.output.repository.MovementRepository;
import com.bank.movement.infrastructure.output.repository.entity.AccountEntity;
import com.bank.movement.infrastructure.output.repository.entity.MovementEntity;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
@Slf4j
public class MovementRepositoryImpl implements MovementRepository {

    private final MovementJPARepository movementJPARepository;

    @Override
    public Mono<Void> createMovement(MovementEntity movementEntity) {
        return Mono.just(movementJPARepository.save(movementEntity)).then()
                .doOnSuccess(aVoid -> log.info("Movement saved successfully: {}", movementEntity))
                .doOnError(error -> log.error("Error saving movement: {}", error.getMessage()));
    }

    @Override
    public Mono<MovementEntity> getMovementById(UUID movementId) {
        log.info("Finding movement by ID: {}", movementId);
        MovementEntity movementEntity = movementJPARepository.findByMovementId(movementId);
        if (movementEntity == null) {
            log.error("Movement not found with ID: {}", movementId);
            return Mono.error(new CustomException(ErrorMessage.CUSTOMER_NOT_FOUND.message(), HttpStatus.NOT_FOUND));
        }

        return Mono.just(movementEntity)
                .doOnNext(movement -> log.info("Movement found: {}", movement))
                .doOnError(error -> log.error("Error finding movement by ID: {}", error.getMessage()));
    }

    @Override
    public Flux<MovementEntity> getAllMovements() {
        log.info("Finding all movements");
        return Flux.fromIterable(movementJPARepository.findAll())
                .doOnNext(movement -> log.info("Movement found: {}", movement))
                .doOnError(error -> log.error("Error finding all movements: {}", error.getMessage()));
    }

    @Override
    public Flux<MovementEntity> getMovementsByAccount(AccountEntity accountEntity) {
        log.info("Finding movements by account: {}", accountEntity);
        return Flux.fromIterable(movementJPARepository.findByAccount(accountEntity))
                .doOnNext(movement -> log.info("Movement found: {}", movement))
                .doOnError(error -> log.error("Error finding movements by account: {}", error.getMessage()));
    }

    @Override
    public Flux<MovementEntity> getMovementsByClientIdAndDate(UUID clientId, LocalDate startDate, LocalDate endDate) {
        log.info("Finding movements by client ID: {} and date range: {} to {}", clientId, startDate, endDate);
        Date startDateSQL = Date.valueOf(startDate);
        Date endDateSQL = Date.valueOf(endDate);
        return Flux.fromIterable(movementJPARepository.findByAccountClientIdAndCreateDateBetween(clientId, startDateSQL, endDateSQL))
                .doOnNext(movement -> log.info("Movement found: {}", movement))
                .doOnError(error -> log.error("Error finding movements by client ID and date range: {}", error.getMessage()));
    }

    @Override
    public Mono<Void> deleteMovementById(UUID movementId) {
        log.info("Deleting movement by ID: {}", movementId);
        return Mono.fromRunnable(() -> {
            try {
                movementJPARepository.deleteById(movementId);
                log.info("Movement deleted successfully: {}", movementId);
            } catch (Exception e) {
                log.error("Error deleting movement: {}", e.getMessage());
                throw new CustomException(ErrorMessage.CUSTOMER_NOT_FOUND.message(), HttpStatus.NOT_FOUND);
            }
        });
    }

}
