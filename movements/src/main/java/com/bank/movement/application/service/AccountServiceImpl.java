package com.bank.movement.application.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.bank.movement.application.input.port.AccountService;
import com.bank.movement.application.output.port.RepositoryAccountPort;
import com.bank.movement.application.util.Utils;
import com.bank.movement.domain.RQCreateAccountDom;
import com.bank.movement.domain.RQUpdateAccountDom;
import com.bank.movement.domain.RSAccountDom;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final RepositoryAccountPort repositoryAccountPort;
    
    @Override
    public Mono<RSAccountDom> getAccountByAccountNumber(String accountNumber) {
        log.info("Fetching account with accountNumber: {}", accountNumber);
        return repositoryAccountPort.findByAccountNumber(accountNumber);
    }

    @Override
    public Flux<RSAccountDom> getAccountsByClientId(UUID clientId) {
        log.info("Fetching accounts for clientId: {}", clientId);
        return repositoryAccountPort.findByClientId(clientId);
    }

    @Override
    public Flux<RSAccountDom> getAllAccounts() {
        log.info("Fetching all accounts");
        return repositoryAccountPort.getAllAccounts();
    }

    @Override
    public Mono<Void> createAccount(RQCreateAccountDom accountDom) {
        log.info("Creating account for clientId: {}", accountDom.getClientId());
        RSAccountDom newAccount = new RSAccountDom(
            null,
            Utils.generateAccountNumber(), 
            accountDom.getAccountType(), 
            accountDom.getInitialBalance(), 
            true, 
            accountDom.getClientId()
        );
        return repositoryAccountPort.saveAccount(newAccount);
    }

    @Override
    public Mono<Void> updateAccount(RQUpdateAccountDom accountDom, String accountNumber) {
        log.info("Updating account: {}", accountNumber);
        RSAccountDom updateAccount = new RSAccountDom(
            null,
            accountNumber,
            accountDom.getAccountType(),
            accountDom.getInitialBalance(),
            accountDom.getAccountStatus(),
            null
        );
        return repositoryAccountPort.updateAccount(updateAccount, accountNumber);
    }

    @Override
    public Mono<Void> deleteAccountByAccountNumber(String accountNumber) {
        log.info("Deleting account: {}", accountNumber);
        return repositoryAccountPort.deleteAccount(accountNumber);
    }
}
