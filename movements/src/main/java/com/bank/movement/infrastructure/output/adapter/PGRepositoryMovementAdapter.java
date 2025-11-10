package com.bank.movement.infrastructure.output.adapter;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import com.bank.movement.application.output.port.RepositoryMovementPort;
import com.bank.movement.domain.RSMovementDom;
import com.bank.movement.domain.enums.MovementType;
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
    private final PGRepositoryMovementMapper mapper;
    private final AccountRepository accountRepository;

    @Override
    public Mono<Void> save(RSMovementDom movementDom) {
        log.info("Attempting to save movement for account: {}", movementDom.getAccountNumber());

        // Validaciones de entrada
        if (movementDom.getMovementValue() == null || movementDom.getMovementValue() <= 0) {
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

        return accountRepository.findByAccountNumber(movementDom.getAccountNumber())
                .switchIfEmpty(Mono.error(new CustomException("Account not found", HttpStatus.NOT_FOUND)))
                .flatMap(account -> {
                    // Crear entidad de movimiento
                    MovementEntity movementEntity = mapper.toEntity(movementDom);
                    movementEntity.setAccount(account);
                    movementEntity.setCreateDate(new Date());
                    movementEntity.setInitialBalance(account.getInitialBalance());

                    // Lógica de negocio: calcular nuevo balance
                    Double newBalance;
                    if (movementDom.getMovementType().equalsIgnoreCase(MovementType.DEPOSIT.toString())) {
                        newBalance = account.getInitialBalance() + movementDom.getMovementValue();
                    } else { // WITHDRAWAL
                        if (account.getInitialBalance() < movementDom.getMovementValue()) {
                            return Mono.error(new CustomException("Insufficient balance", HttpStatus.BAD_REQUEST));
                        }
                        newBalance = account.getInitialBalance() - movementDom.getMovementValue();
                    }

                    movementEntity.setAvailableBalance(newBalance);

                    // Actualizar balance de cuenta
                    account.setInitialBalance(newBalance);

                    // Guardar movimiento y actualizar cuenta
                    return movementRepository.createMovement(movementEntity)
                            .then(accountRepository.updateAccount(account, account.getAccountId()))
                            .doOnSuccess(v -> log.info("Movement created successfully for account: {}", movementDom.getAccountNumber()));
                })
                .onErrorResume(error -> {
                    if (error instanceof CustomException) {
                        return Mono.error(error);
                    }
                    log.error("Error saving movement: {}", error.getMessage());
                    return Mono.error(new CustomException("Failed to save movement", HttpStatus.INTERNAL_SERVER_ERROR));
                });
    }

    @Override
    public Mono<RSMovementDom> findByMovementId(UUID movementId) {
        log.info("Fetching movement by ID: {}", movementId);
        return movementRepository.getMovementById(movementId)
                .map(mapper::toDom)
                .switchIfEmpty(Mono.error(new CustomException("Movement not found", HttpStatus.NOT_FOUND)))
                .doOnError(error -> log.error("Error fetching movement by ID: {}", error.getMessage()));
    }

    @Override
    public Flux<RSMovementDom> findAll() {
        log.info("Fetching all movements");
        return movementRepository.getAllMovements()
                .map(mapper::toDom)
                .doOnNext(movement -> log.info("Movement found: {}", movement))
                .doOnError(error -> log.error("Error fetching all movements: {}", error.getMessage()));
    }

    @Override
    public Flux<RSMovementDom> findByAccountId(UUID accountId) {
        log.info("Fetching movements by account ID: {}", accountId);
        return accountRepository.findByAccountId(accountId)
                .switchIfEmpty(Mono.error(new CustomException("Account not found", HttpStatus.NOT_FOUND)))
                .flatMapMany(account -> movementRepository.getMovementsByAccount(account))
                .map(mapper::toDom)
                .doOnNext(movement -> log.info("Movement found for account ID {}: {}", accountId, movement))
                .doOnError(error -> log.error("Error fetching movements by account ID: {}", error.getMessage()));
    }

    @Override
    public Mono<Void> deleteByMovementId(UUID movementId) {
        log.info("Deleting movement by ID: {}", movementId);
        return movementRepository.getMovementById(movementId)
                .switchIfEmpty(Mono.error(new CustomException("Movement not found", HttpStatus.NOT_FOUND)))
                .flatMap(movement -> movementRepository.deleteMovementById(movementId))
                .doOnSuccess(v -> log.info("Successfully deleted movement by ID: {}", movementId))
                .onErrorResume(error -> {
                    if (error instanceof CustomException) {
                        return Mono.error(error);
                    }
                    log.error("Error deleting movement: {}", error.getMessage());
                    return Mono.error(new CustomException("Failed to delete movement", HttpStatus.INTERNAL_SERVER_ERROR));
                });
    }

    @Override
    public Flux<RSMovementDom> findByClientIdAndDate(UUID clientId, LocalDate startDate, LocalDate endDate) {
        log.info("Fetching movements by client ID: {} and date range: {} to {}", clientId, startDate, endDate);
        return movementRepository.getMovementsByClientIdAndDate(clientId, startDate, endDate)
                .map(mapper::toDom)
                .doOnNext(movement -> log.info("Movement found: {}", movement))
                .doOnError(error -> log.error("Error fetching movements by client ID and date: {}", error.getMessage()));
    }
}
