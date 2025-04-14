package com.bank.movement.infrastructure.output.repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bank.movement.infrastructure.output.repository.entity.AccountEntity;
import com.bank.movement.infrastructure.output.repository.entity.MovementEntity;

@Repository
public interface MovementJPARepository extends JpaRepository<MovementEntity, UUID> {
    MovementEntity findByMovementId(UUID movementId);
    List<MovementEntity> findByAccount(AccountEntity account);
    List<MovementEntity> findByAccountClientIdAndCreateDateBetween(UUID clientId, Date startDate, Date endDate);
    
}