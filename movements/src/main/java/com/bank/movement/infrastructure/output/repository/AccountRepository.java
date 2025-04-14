package com.bank.movement.infrastructure.output.repository;

import java.util.UUID;

import com.bank.movement.infrastructure.output.repository.entity.AccountEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AccountRepository {

    Mono<AccountEntity> findByAccountNumber(String accountNumber);
    Flux<AccountEntity> findByClientId(UUID clientId);
    Mono<AccountEntity> findByAccountNumberAndClientId(String accountNumber, UUID clientId);
    Mono<AccountEntity> findByAccountId(UUID accountId);
    Mono<Void> deleteAccount(UUID accountId);
    Mono<Void> saveAccount(AccountEntity accountEntity);
    Flux<AccountEntity> getAllAccounts();
    Mono<Void> updateAccount(AccountEntity accountEntity, UUID accountId);
}
