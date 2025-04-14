package com.bank.movement.application.input.port;

import java.util.UUID;

import com.bank.movement.domain.RQCreateAccountDom;
import com.bank.movement.domain.RQUpdateAccountDom;
import com.bank.movement.domain.RSAccountDom;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AccountService {
    Mono<RSAccountDom> getAccountByAccountNumber(String accountNumber);
    Flux<RSAccountDom> getAccountsByClientId(UUID clientId);
    Flux<RSAccountDom> getAllAccounts();
    Mono<Void> createAccount(RQCreateAccountDom accountDom);
    Mono<Void> updateAccount(RQUpdateAccountDom accountDom, String accountNumber);
    Mono<Void> deleteAccountByAccountNumber(String accountNumber);

}
