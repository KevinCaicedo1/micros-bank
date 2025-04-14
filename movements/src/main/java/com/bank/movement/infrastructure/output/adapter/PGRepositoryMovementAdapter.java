package com.bank.movement.infrastructure.output.adapter;

import java.time.LocalDate;
import java.util.UUID;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import com.bank.movement.application.output.port.RepositoryMovementPort;
import com.bank.movement.domain.RSMovementDom;
import com.bank.movement.infrastructure.output.adapter.mapper.PGRepositoryMovementMapper;
import com.bank.movement.infrastructure.output.repository.AccountRepository;
import com.bank.movement.infrastructure.output.repository.MovementRepository;
import com.bank.movement.infrastructure.output.repository.entity.MovementEntity;
import com.bank.movement.infrastructure.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.http.HttpStatus;

@Primary
@Service
@RequiredArgsConstructor
@Slf4j
public class PGRepositoryMovementAdapter implements RepositoryMovementPort {

    private final MovementRepository movementRepository;
    private final PGRepositoryMovementMapper pgRepositoryMovementMapper;
    private final AccountRepository accountRepository;

    @Override
    public Mono<Void> save(MovementEntity movementDom) {
        log.info("Attempting to save movement: {}", movementDom.getMovementId());

        return movementRepository.createMovement(movementDom)
                .doOnSuccess(aVoid -> log.info("Movement saved successfully: {}", movementDom))
                .doOnError(error -> {
                    log.error("Error saving movement: {}", error.getMessage());
                    throw new CustomException("Failed to save movement", HttpStatus.INTERNAL_SERVER_ERROR);
                });

    }

    @Override
    public Mono<MovementEntity> findByMovementId(UUID movementId) {
        log.info("Fetching movement by ID: {}", movementId);
        return movementRepository.getMovementById(movementId)
                .switchIfEmpty(Mono.error(new CustomException("Movement not found", HttpStatus.NOT_FOUND)))
                .doOnError(error -> log.error("Error fetching movement by ID: {}", error.getMessage()));
    }

    @Override
    public Flux<MovementEntity> findAll() {
        log.info("Fetching all movements");
        return movementRepository.getAllMovements()
                .doOnNext(movement -> log.info("Movement found: {}", movement))
                .doOnError(error -> log.error("Error fetching all movements: {}", error.getMessage()));
    }

    @Override
    public Flux<MovementEntity> findByAccountId(UUID accountId) {
        log.info("Fetching movements by account ID: {}", accountId);
        return accountRepository.findByAccountId(accountId)
                .switchIfEmpty(Mono.error(new CustomException("Account not found", HttpStatus.NOT_FOUND)))
                .flatMapMany(account -> movementRepository.getMovementsByAccount(account))
                .switchIfEmpty(Mono.error(new CustomException("No movements found for account", HttpStatus.NOT_FOUND)))
                .doOnNext(movement -> log.info("Movement found for account ID {}: {}", accountId, movement))
                .doOnError(error -> log.error("Error fetching movements by account ID: {}", error.getMessage()));
    }

    @Override
    public Mono<Void> deleteByMovementId(UUID movementId) {
        log.info("Deleting movement by ID: {}", movementId);
        return movementRepository.getMovementById(movementId)
                .switchIfEmpty(Mono.error(new CustomException("Movement not found", HttpStatus.NOT_FOUND)))
                .flatMap(movement -> {
                    log.info("Deleting movement: {}", movement);
                    return movementRepository.deleteMovementById(movementId);
                })
                .doOnSuccess(v -> log.info("Successfully deleted movement by ID: {}", movementId))
                .doOnError(error -> {
                    log.error("Error deleting movement: {}", error.getMessage());
                    throw new CustomException("Failed to delete movement", HttpStatus.INTERNAL_SERVER_ERROR);
                });
    }

    @Override
    public Flux<MovementEntity> findByClientIdAndDate(UUID clientId, LocalDate startDate, LocalDate endDate) {
        log.info("Fetching movements by client ID: {} and date range: {} to {}", clientId, startDate, endDate);
        return movementRepository.getMovementsByClientIdAndDate(clientId, startDate, endDate)
                .doOnNext(movement -> log.info("Movement found: {}", movement))
                .doOnError(
                        error -> log.error("Error fetching movements by client ID and date: {}", error.getMessage()));
    }
}
