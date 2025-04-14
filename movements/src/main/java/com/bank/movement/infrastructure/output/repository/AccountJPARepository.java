package com.bank.movement.infrastructure.output.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bank.movement.infrastructure.output.repository.entity.AccountEntity;



@Repository
public interface AccountJPARepository extends JpaRepository<AccountEntity, UUID> {
    
    AccountEntity findByAccountNumber(String accountNumber);
    List<AccountEntity> findByClientId(UUID clientId);
    AccountEntity findByAccountNumberAndClientId(String accountNumber, UUID clientId);
    AccountEntity findByAccountId(UUID accountId);
    
}
