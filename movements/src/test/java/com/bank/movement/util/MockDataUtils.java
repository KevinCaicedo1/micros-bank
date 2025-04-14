package com.bank.movement.util;

import java.util.Date;
import java.util.UUID;
import com.bank.movement.domain.RQCreateMovementDom;
import com.bank.movement.domain.RSMovementDom;
import com.bank.movement.infrastructure.output.repository.entity.AccountEntity;
import com.bank.movement.infrastructure.output.repository.entity.MovementEntity;

public class MockDataUtils {

    public static final String VALID_ACCOUNT_NUMBER = "123456789";
    public static final String INVALID_ACCOUNT_NUMBER = "";
    public static final UUID VALID_MOVEMENT_ID = UUID.randomUUID();
    public static final UUID VALID_ACCOUNT_ID = UUID.randomUUID();
    public static final Double VALID_MOVEMENT_VALUE = 100.0;
    public static final Double INSUFFICIENT_BALANCE = 50.0;
    public static final String MOVEMENT_TYPE_DEPOSIT = "DEPOSIT";
    public static final String MOVEMENT_TYPE_WITHDRAWAL = "WITHDRAWAL";
    
    public static RQCreateMovementDom createValidMovementDOM() {
        return new RQCreateMovementDom(MOVEMENT_TYPE_DEPOSIT, VALID_MOVEMENT_VALUE, VALID_ACCOUNT_NUMBER);
    }

    public static RQCreateMovementDom createInvalidMovementDOM() {
        return new RQCreateMovementDom(null, -1.0, null);
    }

    public static MovementEntity createValidMovementEntity() {
        return MovementEntity.builder()
                .movementId(VALID_MOVEMENT_ID)
                .movementType(MOVEMENT_TYPE_DEPOSIT)
                .movementValue(VALID_MOVEMENT_VALUE)
                .initialBalance(VALID_MOVEMENT_VALUE)
                .availableBalance(VALID_MOVEMENT_VALUE)
                .account(createValidAccountEntity())
                .build();
    }

    public static AccountEntity createValidAccountEntity() {
        return AccountEntity.builder()
                .accountId(VALID_ACCOUNT_ID)
                .accountNumber(VALID_ACCOUNT_NUMBER)
                .initialBalance(VALID_MOVEMENT_VALUE)
                .build();
    }

    public static RSMovementDom createValidRSMovementDom() {
        return new RSMovementDom(VALID_MOVEMENT_ID, new Date(), MOVEMENT_TYPE_DEPOSIT, VALID_MOVEMENT_VALUE, VALID_MOVEMENT_VALUE, VALID_MOVEMENT_VALUE, VALID_ACCOUNT_NUMBER);
    }
}
