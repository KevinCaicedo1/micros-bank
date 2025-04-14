package com.bank.movement.application.service;

import java.util.Date;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.bank.movement.application.input.port.MovementService;
import com.bank.movement.application.output.port.RepositoryAccountPort;
import com.bank.movement.application.output.port.RepositoryMovementPort;
import com.bank.movement.domain.RQCreateMovementDom;
import com.bank.movement.domain.RSMovementDom;
import com.bank.movement.domain.enums.MovementType;
import com.bank.movement.infrastructure.exception.CustomException;
import com.bank.movement.infrastructure.output.adapter.mapper.PGRepositoryMovementMapper;
import com.bank.movement.infrastructure.output.repository.entity.MovementEntity;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class MovementServiceImpl implements MovementService {
    private final RepositoryMovementPort repositoryMovementPort;
    private final PGRepositoryMovementMapper pgMovementMapper;
    private final RepositoryAccountPort repositoryAccountPort;

    @Override
    public Mono<Void> createMovement(RQCreateMovementDom movementDom) {
        log.info("Creating movement for account: {}", movementDom.getAccountNumber());
        if (movementDom.getMovementValue() <= 0) {
            return Mono.error(new CustomException("Movement value must be greater than zero", HttpStatus.BAD_REQUEST));
        }
        if (movementDom.getAccountNumber() == null || movementDom.getAccountNumber().isEmpty()) {
            return Mono.error(new CustomException("Account number is required", HttpStatus.BAD_REQUEST));
        }
        if (movementDom.getMovementType() == null || movementDom.getMovementType().isEmpty()) {
            return Mono.error(new CustomException("Movement type is required", HttpStatus.BAD_REQUEST));
        }
        if (!movementDom.getMovementType().equalsIgnoreCase(MovementType.DEPOSIT.toString()) &&
                !movementDom.getMovementType().equalsIgnoreCase(MovementType.WITHDRAWAL.toString())) {
            return Mono.error(new CustomException("Invalid movement type", HttpStatus.BAD_REQUEST));
        }
        MovementEntity movementEntity = pgMovementMapper.toEntity(new RSMovementDom(
                null, new Date(), movementDom.getMovementType(),
                movementDom.getMovementValue(), 0.0, 0.0, movementDom.getAccountNumber()));

        return repositoryAccountPort.findByAccountNumber(movementDom.getAccountNumber())
                .switchIfEmpty(Mono.error(new CustomException("Account not found", HttpStatus.NOT_FOUND)))
                .flatMap(account -> {
                    movementEntity.setAccount(account);
                    movementEntity.setInitialBalance(account.getInitialBalance());
                    if (movementDom.getMovementType().equalsIgnoreCase(MovementType.DEPOSIT.toString())) {
                        account.setInitialBalance(account.getInitialBalance() + movementDom.getMovementValue());
                    } else if (movementDom.getMovementType().equalsIgnoreCase(MovementType.WITHDRAWAL.toString())) {
                        if (account.getInitialBalance() < movementDom.getMovementValue()) {
                            return Mono.error(new CustomException("Insufficient balance", HttpStatus.BAD_REQUEST));
                        }
                        account.setInitialBalance(account.getInitialBalance() - movementDom.getMovementValue());
                    } else {
                        return Mono.error(new CustomException("Invalid movement type", HttpStatus.BAD_REQUEST));
                    }

                    movementEntity.setAvailableBalance(account.getInitialBalance());
                    
                    return repositoryMovementPort.save(movementEntity)
                            .doOnSuccess(v -> log.info("Movement created successfully for account: {}", movementDom.getAccountNumber()))
                            .doOnError(error -> log.error("Error creating movement: {}", error.getMessage()))
                            .then(repositoryAccountPort.updateAccount(account, account.getAccountId()))
                                    .doOnSuccess(aVoid -> log.info("Account updated successfully: {}", account.getAccountNumber()))
                                    .doOnError(error -> log.error("Error updating account: {}", error.getMessage()));
                })
                .doOnSuccess(
                        v -> log.info("Successfully created movement for account: {}", movementDom.getAccountNumber()))
                .doOnError(error -> log.error("Error creating movement: {}", error.getMessage()));
    }

    @Override
    public Flux<RSMovementDom> getAllMovements() {
        log.info("Fetching all movements");
        return repositoryMovementPort.findAll()
                .map(pgMovementMapper::toDom)
                .doOnError(error -> log.error("Error fetching movements: {}", error.getMessage()));
    }

    @Override
    public Mono<RSMovementDom> getMovementById(UUID movementId) {
        log.info("Fetching movement by ID: {}", movementId);
        return repositoryMovementPort.findByMovementId(movementId)
                .flatMap(entity -> Mono.just(pgMovementMapper.toDom(entity)))
                .switchIfEmpty(Mono.error(new CustomException("Movement not found", HttpStatus.NOT_FOUND)))
                .doOnError(error -> log.error("Error fetching movement: {}", error.getMessage()));
    }

    @Override
    public Flux<RSMovementDom> getMovementsByAccount(String accountNumber) {
        log.info("Fetching movements for account: {}", accountNumber);

        return repositoryAccountPort.findByAccountNumber(accountNumber)
                .switchIfEmpty(Mono.error(new CustomException("Account not found", HttpStatus.NOT_FOUND)))
                .flatMapMany(account -> repositoryMovementPort.findByAccountId(account.getAccountId())
                        .map(pgMovementMapper::toDom))
                .doOnNext(movement -> log.info("Movement found: {}", movement))
                .doOnError(error -> log.error("Error fetching movements: {}", error.getMessage()));
    }

    @Override
    public Mono<Void> deleteMovementById(UUID movementId) {
        log.info("Deleting movement by ID: {}", movementId);
        return repositoryMovementPort.deleteByMovementId(movementId)
                .doOnError(error -> log.error("Error deleting movement: {}", error.getMessage()));

    }

}
