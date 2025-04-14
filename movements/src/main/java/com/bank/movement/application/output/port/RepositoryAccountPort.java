package com.bank.movement.application.output.port;

import java.util.UUID;

import com.bank.movement.infrastructure.output.repository.entity.AccountEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RepositoryAccountPort {
    Mono<Void> saveAccount(AccountEntity accountEntity);
    Mono<AccountEntity> findByAccountNumber(String accountNumber);
    Flux<AccountEntity> findByClientId(UUID clientId);
    Flux<AccountEntity> getAllAccounts();
    Mono<AccountEntity> findByAccountNumberAndClientId(String accountNumber, UUID clientId);
    Mono<AccountEntity> findByAccountId(UUID accountId);
    Mono<Void> deleteAccount(UUID accountId);
    Mono<Void> updateAccount(AccountEntity accountEntity, UUID accountId);
}
