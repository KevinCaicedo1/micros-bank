package com.bank.movement.infrastructure.output.repository.impl;

import java.util.UUID;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import com.bank.movement.infrastructure.output.repository.AccountJPARepository;
import com.bank.movement.infrastructure.output.repository.AccountRepository;
import com.bank.movement.infrastructure.output.repository.entity.AccountEntity;
import com.bank.movement.infrastructure.exception.CustomException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
@Slf4j
public class AccountRepositoryImpl implements AccountRepository {

    private final AccountJPARepository accountJPARepository;

    @Override
    public Mono<AccountEntity> findByAccountNumber(String accountNumber) {
        return Mono.fromCallable(() -> {
            try {
                log.info("Searching for account with accountNumber: {}", accountNumber);
                AccountEntity account = accountJPARepository.findByAccountNumber(accountNumber);
                if (account == null) {
                    throw new CustomException("Account not found", HttpStatus.NOT_FOUND);
                }
                return account;
            } catch (DataAccessException e) {
                log.error("Error accessing the database: {}", e.getMessage());
                throw new CustomException("Database access error", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        });
    }

    @Override
    public Flux<AccountEntity> findByClientId(UUID clientId) {
        return Flux.defer(() -> {
            try {
                log.info("Searching for accounts with clientId: {}", clientId);
                return Flux.fromIterable(accountJPARepository.findByClientId(clientId));
            } catch (DataAccessException e) {
                log.error("Error accessing the database: {}", e.getMessage());
                return Flux.error(new CustomException("Database access error", HttpStatus.INTERNAL_SERVER_ERROR));
            }
        });
    }

    @Override
    public Mono<AccountEntity> findByAccountNumberAndClientId(String accountNumber, UUID clientId) {
        return Mono.fromCallable(() -> {
            try {
                log.info("Searching for account with accountNumber: {} and clientId: {}", accountNumber, clientId);
                AccountEntity account = accountJPARepository.findByAccountNumberAndClientId(accountNumber, clientId);
                if (account == null) {
                    throw new CustomException("Account not found", HttpStatus.NOT_FOUND);
                }
                return account;
            } catch (DataAccessException e) {
                log.error("Error accessing the database: {}", e.getMessage());
                throw new CustomException("Database access error", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        });
    }

    @Override
    public Mono<AccountEntity> findByAccountId(UUID accountId) {
        return Mono.fromCallable(() -> {
            try {
                log.info("Searching for account with accountId: {}", accountId);
                AccountEntity account = accountJPARepository.findByAccountId(accountId);
                if (account == null) {
                    throw new CustomException("Account not found", HttpStatus.NOT_FOUND);
                }
                return account;
            } catch (DataAccessException e) {
                log.error("Error accessing the database: {}", e.getMessage());
                throw new CustomException("Database access error", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        });
    }

    @Override
    public Mono<Void> deleteAccount(UUID accountId) {
        return Mono.fromRunnable(() -> {
            try {
                log.info("Deleting account with accountId: {}", accountId);
                accountJPARepository.deleteById(accountId);
            } catch (DataAccessException e) {
                log.error("Error accessing the database: {}", e.getMessage());
                throw new CustomException("Failed to delete account", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }).then();
    }

    @Override
    public Mono<Void> saveAccount(AccountEntity accountEntity) {
        return Mono.fromRunnable(() -> {
            try {
                log.info("Saving account with accountNumber: {}", accountEntity.getAccountNumber());
                accountJPARepository.save(accountEntity);
            } catch (DataAccessException e) {
                log.error("Error accessing the database: {}", e.getMessage());
                throw new CustomException("Failed to save account", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }).then();
    }

    @Override
    public Flux<AccountEntity> getAllAccounts() {
        return Flux.defer(() -> {
            try {
                log.info("Fetching all accounts");
                return Flux.fromIterable(accountJPARepository.findAll());
            } catch (DataAccessException e) {
                log.error("Error accessing the database: {}", e.getMessage());
                return Flux.error(new CustomException("Database access error", HttpStatus.INTERNAL_SERVER_ERROR));
            }
        });
    }

    @Override
    public Mono<Void> updateAccount(AccountEntity accountEntity, UUID accountId) {
        return Mono.fromRunnable(() -> {
            try {
                log.info("Updating account with accountId: {}", accountId);
                AccountEntity existingAccount = accountJPARepository.findByAccountId(accountId);
                if (existingAccount == null) {
                    throw new CustomException("Account not found", HttpStatus.NOT_FOUND);
                }
                existingAccount.setAccountNumber(accountEntity.getAccountNumber());
                existingAccount.setClientId(accountEntity.getClientId());
                existingAccount.setInitialBalance(accountEntity.getInitialBalance());
                accountJPARepository.save(existingAccount);
            } catch (DataAccessException e) {
                log.error("Error accessing the database: {}", e.getMessage());
                throw new CustomException("Failed to update account", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }).then();
    }
}
