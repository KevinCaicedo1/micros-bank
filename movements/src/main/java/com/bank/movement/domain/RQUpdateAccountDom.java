package com.bank.movement.domain;
/*
      String accountType;
     Double initialBalance;
     Boolean accountStatus;
 */
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
public class RQUpdateAccountDom {

     String accountType;
     Double initialBalance;
     Boolean accountStatus;

}
