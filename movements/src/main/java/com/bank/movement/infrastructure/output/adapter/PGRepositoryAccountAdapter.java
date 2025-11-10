package com.bank.movement.infrastructure.output.adapter;

import java.util.UUID;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.bank.movement.application.output.port.RepositoryAccountPort;
import com.bank.movement.domain.RSAccountDom;
import com.bank.movement.infrastructure.output.repository.AccountRepository;
import com.bank.movement.infrastructure.output.adapter.mapper.PGRepositoryAccountMapper;
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
    private final PGRepositoryAccountMapper mapper;

    @Override
    public Mono<Void> saveAccount(RSAccountDom accountDom) {
        log.info("Attempting to save account: {}", accountDom.getAccountNumber());
        return accountRepository.saveAccount(mapper.toEntity(accountDom))
            .doOnSuccess(aVoid -> log.info("Successfully saved account: {}", accountDom.getAccountNumber()))
            .onErrorResume(error -> {
                log.error("Error saving account: {}", error.getMessage());
                return Mono.error(new CustomException("Failed to save account", HttpStatus.INTERNAL_SERVER_ERROR));
            });
    }

    @Override
    public Mono<RSAccountDom> findByAccountNumber(String accountNumber) {
        log.info("Fetching account with accountNumber: {}", accountNumber);
        return accountRepository.findByAccountNumber(accountNumber)
            .map(mapper::toDom)
            .switchIfEmpty(Mono.error(new CustomException("Account not found", HttpStatus.NOT_FOUND)))
            .doOnError(error -> log.error("Error fetching account by number: {}", error.getMessage()));
    }

    @Override
    public Flux<RSAccountDom> findByClientId(UUID clientId) {
        log.info("Fetching accounts for clientId: {}", clientId);
        return accountRepository.findByClientId(clientId)
            .map(mapper::toDom)
            .doOnError(error -> log.error("Error fetching accounts by client ID: {}", error.getMessage()));
    }

    @Override
    public Mono<RSAccountDom> findByAccountNumberAndClientId(String accountNumber, UUID clientId) {
        log.info("Fetching account with accountNumber: {} and clientId: {}", accountNumber, clientId);
        return accountRepository.findByAccountNumberAndClientId(accountNumber, clientId)
            .map(mapper::toDom)
            .switchIfEmpty(Mono.error(new CustomException("Account not found by client and number", HttpStatus.NOT_FOUND)))
            .doOnError(error -> log.error("Error fetching account by client and number: {}", error.getMessage()));
    }

    @Override
    public Mono<RSAccountDom> findByAccountId(UUID accountId) {
        log.info("Fetching account with accountId: {}", accountId);
        return accountRepository.findByAccountId(accountId)
            .map(mapper::toDom)
            .switchIfEmpty(Mono.error(new CustomException("Account not found", HttpStatus.NOT_FOUND)))
            .doOnError(error -> log.error("Error fetching account by ID: {}", error.getMessage()));
    }

    @Override
    public Mono<Void> deleteAccount(String accountNumber) {
        log.info("Deleting account with accountNumber: {}", accountNumber);
        return accountRepository.findByAccountNumber(accountNumber)
            .switchIfEmpty(Mono.error(new CustomException("Account not found", HttpStatus.NOT_FOUND)))
            .flatMap(account -> accountRepository.deleteAccount(account.getAccountId()))
            .doOnSuccess(aVoid -> log.info("Successfully deleted account: {}", accountNumber))
            .onErrorResume(error -> {
                log.error("Error deleting account: {}", error.getMessage());
                return Mono.error(new CustomException("Failed to delete account", HttpStatus.INTERNAL_SERVER_ERROR));
            });
    }

    @Override
    public Flux<RSAccountDom> getAllAccounts() {
        log.info("Fetching all accounts");
        return accountRepository.getAllAccounts()
            .map(mapper::toDom)
            .doOnError(error -> log.error("Error fetching all accounts: {}", error.getMessage()));
    }

    @Override
    public Mono<Void> updateAccount(RSAccountDom accountDom, String accountNumber) {
        log.info("Updating account with accountNumber: {}", accountNumber);
        return accountRepository.findByAccountNumber(accountNumber)
            .switchIfEmpty(Mono.error(new CustomException("Account not found", HttpStatus.NOT_FOUND)))
            .flatMap(existingAccount -> {
                existingAccount.setAccountType(accountDom.getAccountType());
                existingAccount.setInitialBalance(accountDom.getInitialBalance());
                existingAccount.setAccountStatus(accountDom.getAccountStatus());
                return accountRepository.updateAccount(existingAccount, existingAccount.getAccountId());
            })
            .doOnSuccess(aVoid -> log.info("Successfully updated account: {}", accountNumber))
            .onErrorResume(error -> {
                log.error("Error updating account: {}", error.getMessage());
                return Mono.error(new CustomException("Failed to update account", HttpStatus.INTERNAL_SERVER_ERROR));
            });
    }
}
