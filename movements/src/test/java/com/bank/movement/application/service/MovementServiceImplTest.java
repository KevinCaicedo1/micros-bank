package com.bank.movement.application.service;

import static org.mockito.Mockito.*;

import com.bank.movement.application.output.port.RepositoryAccountPort;
import com.bank.movement.application.output.port.RepositoryMovementPort;
import com.bank.movement.domain.RQCreateMovementDom;
import com.bank.movement.domain.RSMovementDom;
import com.bank.movement.infrastructure.exception.CustomException;
import com.bank.movement.infrastructure.output.adapter.mapper.PGRepositoryMovementMapper;
import com.bank.movement.infrastructure.output.repository.entity.MovementEntity;
import com.bank.movement.util.MockDataUtils;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
public class MovementServiceImplTest {

    @Mock
    private RepositoryMovementPort repositoryMovementPort;
    
    @Mock
    private PGRepositoryMovementMapper pgMovementMapper;
    
    @Mock
    private RepositoryAccountPort repositoryAccountPort;
    
    @InjectMocks
    private MovementServiceImpl movementService;

    @BeforeEach
    void setUp() {
        // Los mocks se inicializan automáticamente con @ExtendWith(MockitoExtension.class)
    }

    @Test
    void givenValidMovementDomShouldCreateMovement() {
        // Given
        RQCreateMovementDom validMovementDom = MockDataUtils.createValidMovementDOM();
        when(repositoryAccountPort.findByAccountNumber(validMovementDom.getAccountNumber()))
                .thenReturn(Mono.just(MockDataUtils.createValidAccountEntity()));
        when(pgMovementMapper.toEntity(any(RSMovementDom.class)))
                .thenReturn(MockDataUtils.createValidMovementEntity());
        when(repositoryMovementPort.save(any(MovementEntity.class)))
                .thenReturn(Mono.empty());
        when(repositoryAccountPort.updateAccount(any(), any(UUID.class)))
                .thenReturn(Mono.empty());

        // When
        Mono<Void> result = movementService.createMovement(validMovementDom);

        // Then
        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    void givenInvalidMovementDomWhenCreatingMovementThenThrowCustomException() {
        // Given
        RQCreateMovementDom invalidMovementDom = MockDataUtils.createInvalidMovementDOM();

        // When
        Mono<Void> result = movementService.createMovement(invalidMovementDom);

        // Then
        StepVerifier.create(result)
                .expectError(CustomException.class)
                .verify();
    }

    @Test
    void givenAccountRepositoryThrowsExceptionWhenCreatingMovementThenThrowCustomException() {
        // Given
        RQCreateMovementDom validMovementDom = MockDataUtils.createValidMovementDOM();
        when(repositoryAccountPort.findByAccountNumber(anyString()))
                .thenReturn(Mono.error(new CustomException("Account not found", HttpStatus.NOT_FOUND)));

        // When
        Mono<Void> result = movementService.createMovement(validMovementDom);

        // Then
        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof CustomException &&
                        throwable.getMessage().equals("Account not found"))
                .verify();
    }

    @Test
    void givenValidAccountNumberWhenFetchingMovementsByAccountThenReturnFluxOfMovements() {
        // Given
        String validAccountNumber = MockDataUtils.VALID_ACCOUNT_NUMBER;
        when(repositoryAccountPort.findByAccountNumber(anyString()))
                .thenReturn(Mono.just(MockDataUtils.createValidAccountEntity()));
        when(repositoryMovementPort.findByAccountId(any(UUID.class)))
                .thenReturn(Flux.just(MockDataUtils.createValidMovementEntity()));
        when(pgMovementMapper.toDom(any(MovementEntity.class)))
                .thenReturn(MockDataUtils.createValidRSMovementDom());

        // When
        Flux<RSMovementDom> result = movementService.getMovementsByAccount(validAccountNumber);

        // Then
        StepVerifier.create(result)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void givenInvalidAccountNumberWhenFetchingMovementsByAccountThenThrowCustomException() {
        // Given
        String invalidAccountNumber = MockDataUtils.INVALID_ACCOUNT_NUMBER;
        when(repositoryAccountPort.findByAccountNumber(anyString()))
                .thenReturn(Mono.error(new CustomException("Account not found", HttpStatus.NOT_FOUND)));

        // When
        Flux<RSMovementDom> result = movementService.getMovementsByAccount(invalidAccountNumber);

        // Then
        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof CustomException &&
                        throwable.getMessage().equals("Account not found"))
                .verify();
    }
}
