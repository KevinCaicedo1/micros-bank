package com.bank.movement.infrastructure.output.adapter;

import java.util.UUID;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.bank.movement.application.output.port.RepositoryAccountPort;
import com.bank.movement.infrastructure.output.repository.AccountRepository;
import com.bank.movement.infrastructure.output.repository.entity.AccountEntity;
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
public class PGRepositoryAccountAdapter implements RepositoryAccountPort {

    private final AccountRepository accountRepository;

    @Override
    public Mono<Void> saveAccount(AccountEntity accountEntity) {
        log.info("Attempting to save account: {}", accountEntity.getAccountNumber());
        return accountRepository.saveAccount(accountEntity)
            .doOnSuccess(aVoid -> log.info("Successfully saved account: {}", accountEntity.getAccountNumber()))
            .doOnError(error -> {
                log.error("Error saving account: {}", error.getMessage());
                throw new CustomException("Failed to save account", HttpStatus.INTERNAL_SERVER_ERROR);
            });
    }

    @Override
    public Mono<AccountEntity> findByAccountNumber(String accountNumber) {
        log.info("Fetching account with accountNumber: {}", accountNumber);
        return accountRepository.findByAccountNumber(accountNumber)
            .switchIfEmpty(Mono.error(new CustomException("Account not found", HttpStatus.NOT_FOUND)))
            .doOnError(error -> log.error("Error fetching account by number: {}", error.getMessage()));
    }

    @Override
    public Flux<AccountEntity> findByClientId(UUID clientId) {
        log.info("Fetching accounts for clientId: {}", clientId);
        return accountRepository.findByClientId(clientId)
            .doOnError(error -> log.error("Error fetching accounts by client ID: {}", error.getMessage()));
    }

    @Override
    public Mono<AccountEntity> findByAccountNumberAndClientId(String accountNumber, UUID clientId) {
        log.info("Fetching account with accountNumber: {} and clientId: {}", accountNumber, clientId);
        return accountRepository.findByAccountNumberAndClientId(accountNumber, clientId)
            .switchIfEmpty(Mono.error(new CustomException("Account not found by client and number", HttpStatus.NOT_FOUND)))
            .doOnError(error -> log.error("Error fetching account by client and number: {}", error.getMessage()));
    }

    @Override
    public Mono<AccountEntity> findByAccountId(UUID accountId) {
        log.info("Fetching account with accountId: {}", accountId);
        return accountRepository.findByAccountId(accountId)
            .switchIfEmpty(Mono.error(new CustomException("Account not found", HttpStatus.NOT_FOUND)))
            .doOnError(error -> log.error("Error fetching account by ID: {}", error.getMessage()));
    }

    @Override
    public Mono<Void> deleteAccount(UUID accountId) {
        log.info("Deleting account with accountId: {}", accountId);
        return accountRepository.deleteAccount(accountId)
            .doOnSuccess(aVoid -> log.info("Successfully deleted account with ID: {}", accountId))
            .doOnError(error -> {
                log.error("Error deleting account: {}", error.getMessage());
                throw new CustomException("Failed to delete account", HttpStatus.INTERNAL_SERVER_ERROR);
            });
    }

    @Override
    public Flux<AccountEntity> getAllAccounts() {
        log.info("Fetching all accounts");
        return accountRepository.getAllAccounts()
            .doOnError(error -> log.error("Error fetching all accounts: {}", error.getMessage()));
    }

    @Override
    public Mono<Void> updateAccount(AccountEntity accountEntity, UUID accountId) {
        log.info("Updating account with ID: {}", accountId);
        return accountRepository.updateAccount(accountEntity, accountId)
            .doOnSuccess(aVoid -> log.info("Successfully updated account with ID: {}", accountId))
            .doOnError(error -> {
                log.error("Error updating account: {}", error.getMessage());
                throw new CustomException("Failed to update account", HttpStatus.INTERNAL_SERVER_ERROR);
            });
       
    }
}
