package com.bank.movement.application.service;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.bank.movement.application.input.port.AccountService;
import com.bank.movement.application.output.port.RepositoryAccountPort;
import com.bank.movement.application.util.Utils;
import com.bank.movement.domain.RQCreateAccountDom;
import com.bank.movement.domain.RQUpdateAccountDom;
import com.bank.movement.domain.RSAccountDom;
import com.bank.movement.infrastructure.exception.CustomException;
import com.bank.movement.infrastructure.output.adapter.mapper.PGRepositoryAccountMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final RepositoryAccountPort repositoryAccountPort;
    private final PGRepositoryAccountMapper pgRepositoryAccountMapper;
    
    @Override
    public Mono<RSAccountDom> getAccountByAccountNumber(String accountNumber) {
        log.info("Fetching account with accountNumber: {}", accountNumber);
        return repositoryAccountPort.findByAccountNumber(accountNumber)
            .switchIfEmpty(Mono.error(new CustomException("Account not found", HttpStatus.NOT_FOUND)))
            .map(pgRepositoryAccountMapper::toDom)
            .doOnError(error -> log.error("Error fetching account: {}", error.getMessage()));
    }

    @Override
    public Flux<RSAccountDom> getAccountsByClientId(UUID clientId) {
        log.info("Fetching accounts for clientId: {}", clientId);
        return repositoryAccountPort.findByClientId(clientId)
            .map(pgRepositoryAccountMapper::toDom)
            .doOnError(error -> log.error("Error fetching accounts: {}", error.getMessage()));
    }

    @Override
    public Flux<RSAccountDom> getAllAccounts() {
        log.info("Fetching all accounts");
        return repositoryAccountPort.getAllAccounts() // Assuming findByClientId(null) fetches all accounts
            .map(pgRepositoryAccountMapper::toDom)
            .doOnError(error -> log.error("Error fetching all accounts: {}", error.getMessage()));
    }

    @Override
    public Mono<Void> createAccount(RQCreateAccountDom accountDom) {
        log.info("Creating account for clientId: {}", accountDom.getClientId());
        return Mono.just(accountDom)
            .map(dom -> pgRepositoryAccountMapper.toEntity(new RSAccountDom(
                Utils.generateAccountNumber(), dom.getAccountType(), dom.getInitialBalance(), true, dom.getClientId())))
            .flatMap(repositoryAccountPort::saveAccount)
            .onErrorResume(error -> {
                log.error("Error creating account: {}", error.getMessage());
                return Mono.error(new CustomException("Failed to create account", HttpStatus.INTERNAL_SERVER_ERROR));
            });
    }

    @Override
    public Mono<Void> updateAccount(RQUpdateAccountDom accountDom, String accountNumber) {
        log.info("Updating account");
        return repositoryAccountPort.findByAccountNumber(accountNumber)
            .switchIfEmpty(Mono.error(new CustomException("Account not found", HttpStatus.NOT_FOUND)))
            .map(entity -> {
                entity.setAccountType(accountDom.getAccountType());
                entity.setInitialBalance(accountDom.getInitialBalance());
                entity.setAccountStatus(accountDom.getAccountStatus());
                return entity;
            })
            .flatMap(repositoryAccountPort::saveAccount)
            .doOnError(error -> log.error("Error updating account: {}", error.getMessage()));
    }

    @Override
    public Mono<Void> deleteAccountByAccountNumber(String accountNumber) {
        log.info("Deleting account with accountNumber: {}", accountNumber);
        return repositoryAccountPort.findByAccountNumber(accountNumber)
            .switchIfEmpty(Mono.error(new CustomException("Account not found", HttpStatus.NOT_FOUND)))
            .flatMap(entity -> repositoryAccountPort.deleteAccount(entity.getAccountId()))
            .doOnSuccess(aVoid -> log.info("Account deleted successfully: {}", accountNumber))
            .doOnError(error -> log.error("Error deleting account: {}", error.getMessage()));
    }

}
