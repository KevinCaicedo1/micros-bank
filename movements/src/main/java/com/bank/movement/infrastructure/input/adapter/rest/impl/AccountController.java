package com.bank.movement.infrastructure.input.adapter.rest.impl;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import com.bank.movement.application.input.port.AccountService;
import com.bank.movement.infrastructure.input.adapter.rest.AccountsApi;
import com.bank.movement.infrastructure.input.adapter.rest.mapper.AccountMapper;
import com.bank.movement.infrastructure.input.adapter.rest.model.RQCreateAccount;
import com.bank.movement.infrastructure.input.adapter.rest.model.RQUpdateAccount;
import com.bank.movement.infrastructure.input.adapter.rest.model.RSAccount;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@AllArgsConstructor
@Slf4j
@Validated
@RequestMapping("/api/v1")
public class AccountController implements AccountsApi {

    private final AccountService accountService;
    private final AccountMapper accountMapper;

    @Override
    public Mono<ResponseEntity<Void>> accountsAccountNumberDelete(String accountNumber, ServerWebExchange exchange) {
        log.info("Request to delete account with accountNumber: {}", accountNumber);
        return accountService.deleteAccountByAccountNumber(accountNumber)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }

    @Override
    public Mono<ResponseEntity<RSAccount>> accountsAccountNumberGet(String accountNumber, ServerWebExchange exchange) {
        log.info("Request to get account with accountNumber: {}", accountNumber);
        return accountService.getAccountByAccountNumber(accountNumber)
                .map(accountMapper::toAccount)
                .map(ResponseEntity::ok)
                .doOnError(error -> log.error("Error fetching account: {}", error.getMessage()))
                .onErrorResume(e -> Mono.just(ResponseEntity.notFound().build())); // Returns 404 if not found
    }

    @Override
    public Mono<ResponseEntity<RSAccount>> accountsAccountNumberPut(String accountNumber,
            @Valid Mono<RQUpdateAccount> rqUpdateAccount, ServerWebExchange exchange) {
        log.info("Request to update account: {}", accountNumber);

        return rqUpdateAccount.flatMap(req -> {
            return accountService.updateAccount(accountMapper.toDom(req), accountNumber)
                    .then(accountService.getAccountByAccountNumber(accountNumber))
                    .map(accountMapper::toAccount)
                    .map(ResponseEntity::ok);
        })
                .doOnError(error -> log.error("Error updating account: {}", error.getMessage()))
                .onErrorResume(e -> Mono.just(ResponseEntity.internalServerError().build()));
    }

    @Override
    public Mono<ResponseEntity<Flux<RSAccount>>> accountsClientClientIdGet(UUID clientId, ServerWebExchange exchange) {
        log.info("Request to get accounts for clientId: {}", clientId);
        return Mono.just(accountService.getAccountsByClientId(clientId)
                .map(accountMapper::toAccount)
                .doOnError(error -> log.error("Error fetching accounts: {}", error.getMessage())))
                .map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<Flux<RSAccount>>> accountsGet(ServerWebExchange exchange) {
        log.info("Request to get all accounts");
        return Mono.just(accountService.getAllAccounts()
                .map(accountMapper::toAccount)
                .doOnError(error -> log.error("Error fetching all accounts: {}", error.getMessage())))
                .map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<Void>> accountsPost(@Valid Mono<RQCreateAccount> rqCreateAccount,
            ServerWebExchange exchange) {
        log.info("Request to create account");
        return rqCreateAccount
                .map(accountMapper::toDom)
                .flatMap(accountService::createAccount)
                .map(ResponseEntity.status(HttpStatus.CREATED)::body)
                .doOnError(error -> log.error("Error creating account: {}", error.getMessage()));
    }
}
