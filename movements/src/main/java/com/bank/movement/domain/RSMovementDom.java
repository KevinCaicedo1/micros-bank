package com.bank.movement.domain;
import java.util.Date;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.Generated;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Generated
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class RSMovementDom {
     UUID movementId;
     Date createDate;
     String movementType;
     Double movementValue;
     Double initialBalance;
     Double availableBalance;
     String accountNumber;
}
