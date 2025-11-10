package com.bank.movement.application.service;

import static org.mockito.Mockito.*;

import com.bank.movement.application.output.port.RepositoryAccountPort;
import com.bank.movement.application.output.port.RepositoryMovementPort;
import com.bank.movement.domain.RQCreateMovementDom;
import com.bank.movement.domain.RSAccountDom;
import com.bank.movement.domain.RSMovementDom;
import com.bank.movement.infrastructure.exception.CustomException;
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
        
        // El servicio ahora solo crea el RSMovementDom y delega al puerto
        // El puerto (adaptador) maneja todas las validaciones y lógica de negocio
        when(repositoryMovementPort.save(any(RSMovementDom.class)))
                .thenReturn(Mono.empty());

        // When
        Mono<Void> result = movementService.createMovement(validMovementDom);

        // Then
        StepVerifier.create(result)
                .verifyComplete();
        
        // Verificar que se llamó al puerto con un RSMovementDom
        verify(repositoryMovementPort, times(1)).save(any(RSMovementDom.class));
    }

    @Test
    void givenInvalidMovementDomWhenCreatingMovementThenThrowCustomException() {
        // Given
        RQCreateMovementDom invalidMovementDom = MockDataUtils.createInvalidMovementDOM();
        
        // Las validaciones ahora están en el adaptador, así que el adaptador retorna el error
        when(repositoryMovementPort.save(any(RSMovementDom.class)))
                .thenReturn(Mono.error(new CustomException("Movement value must be greater than zero", HttpStatus.BAD_REQUEST)));

        // When
        Mono<Void> result = movementService.createMovement(invalidMovementDom);

        // Then
        StepVerifier.create(result)
                .expectError(CustomException.class)
                .verify();
    }

    @Test
    void givenAccountNotFoundWhenCreatingMovementThenThrowCustomException() {
        // Given
        RQCreateMovementDom validMovementDom = MockDataUtils.createValidMovementDOM();
        
        // El adaptador valida que la cuenta exista y retorna error si no existe
        when(repositoryMovementPort.save(any(RSMovementDom.class)))
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
        RSAccountDom accountDom = new RSAccountDom(
            MockDataUtils.VALID_ACCOUNT_ID,
            validAccountNumber,
            "SAVINGS",
            100.0,
            true,
            UUID.randomUUID()
        );
        
        when(repositoryAccountPort.findByAccountNumber(validAccountNumber))
                .thenReturn(Mono.just(accountDom));
        when(repositoryMovementPort.findByAccountId(MockDataUtils.VALID_ACCOUNT_ID))
                .thenReturn(Flux.just(MockDataUtils.createValidRSMovementDom()));

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
        
        // El adaptador lanza la excepción cuando no encuentra la cuenta
        when(repositoryAccountPort.findByAccountNumber(invalidAccountNumber))
                .thenReturn(Mono.error(new CustomException("Account not found", HttpStatus.NOT_FOUND)));

        // When
        Flux<RSMovementDom> result = movementService.getMovementsByAccount(invalidAccountNumber);

        // Then
        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof CustomException &&
                        throwable.getMessage().equals("Account not found"))
                .verify();
    }
    
    @Test
    void givenValidMovementIdWhenGettingMovementByIdThenReturnMovement() {
        // Given
        UUID movementId = MockDataUtils.VALID_MOVEMENT_ID;
        RSMovementDom expectedMovement = MockDataUtils.createValidRSMovementDom();
        
        when(repositoryMovementPort.findByMovementId(movementId))
                .thenReturn(Mono.just(expectedMovement));

        // When
        Mono<RSMovementDom> result = movementService.getMovementById(movementId);

        // Then
        StepVerifier.create(result)
                .expectNext(expectedMovement)
                .verifyComplete();
    }
    
    @Test
    void givenMovementIdWhenDeletingMovementThenCompleteSuccessfully() {
        // Given
        UUID movementId = MockDataUtils.VALID_MOVEMENT_ID;
        
        when(repositoryMovementPort.deleteByMovementId(movementId))
                .thenReturn(Mono.empty());

        // When
        Mono<Void> result = movementService.deleteMovementById(movementId);

        // Then
        StepVerifier.create(result)
                .verifyComplete();
        
        verify(repositoryMovementPort, times(1)).deleteByMovementId(movementId);
    }
    
    @Test
    void whenGettingAllMovementsThenReturnFluxOfMovements() {
        // Given
        when(repositoryMovementPort.findAll())
                .thenReturn(Flux.just(
                    MockDataUtils.createValidRSMovementDom(),
                    MockDataUtils.createValidRSMovementDom()
                ));

        // When
        Flux<RSMovementDom> result = movementService.getAllMovements();

        // Then
        StepVerifier.create(result)
                .expectNextCount(2)
                .verifyComplete();
    }
}
