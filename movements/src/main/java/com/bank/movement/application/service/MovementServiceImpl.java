package com.bank.movement.application.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.bank.movement.application.input.port.MovementService;
import com.bank.movement.application.output.port.RepositoryAccountPort;
import com.bank.movement.application.output.port.RepositoryMovementPort;
import com.bank.movement.domain.RQCreateMovementDom;
import com.bank.movement.domain.RSMovementDom;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class MovementServiceImpl implements MovementService {
    
    private final RepositoryMovementPort repositoryMovementPort;
    private final RepositoryAccountPort repositoryAccountPort;

    @Override
    public Mono<Void> createMovement(RQCreateMovementDom movementDom) {
        log.info("Creating movement for account: {}", movementDom.getAccountNumber());
        RSMovementDom newMovement = new RSMovementDom(
            null,
            null,
            movementDom.getMovementType(),
            movementDom.getMovementValue(),
            null,
            null,
            movementDom.getAccountNumber()
        );
        return repositoryMovementPort.save(newMovement);
    }

    @Override
    public Flux<RSMovementDom> getAllMovements() {
        log.info("Fetching all movements");
        return repositoryMovementPort.findAll();
    }

    @Override
    public Mono<RSMovementDom> getMovementById(UUID movementId) {
        log.info("Fetching movement by ID: {}", movementId);
        return repositoryMovementPort.findByMovementId(movementId);
    }

    @Override
    public Flux<RSMovementDom> getMovementsByAccount(String accountNumber) {
        log.info("Fetching movements for account: {}", accountNumber);
        return repositoryAccountPort.findByAccountNumber(accountNumber)
            .flatMapMany(account -> repositoryMovementPort.findByAccountId(account.getAccountId()));
    }

    @Override
    public Mono<Void> deleteMovementById(UUID movementId) {
        log.info("Deleting movement by ID: {}", movementId);
        return repositoryMovementPort.deleteByMovementId(movementId);
    }
}
