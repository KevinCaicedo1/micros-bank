package com.bank.movement.application.output.port;

import java.util.UUID;

import com.bank.movement.domain.RSAccountDom;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RepositoryAccountPort {
    Mono<Void> saveAccount(RSAccountDom accountEntity);
    Mono<RSAccountDom> findByAccountNumber(String accountNumber);
    Flux<RSAccountDom> findByClientId(UUID clientId);
    Flux<RSAccountDom> getAllAccounts();
    Mono<RSAccountDom> findByAccountNumberAndClientId(String accountNumber, UUID clientId);
    Mono<RSAccountDom> findByAccountId(UUID accountId);
    Mono<Void> deleteAccount(String accountId);
    Mono<Void> updateAccount(RSAccountDom accountEntity, String accountId);
}
