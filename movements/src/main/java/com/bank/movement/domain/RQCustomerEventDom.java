package com.bank.movement.domain;

import java.util.UUID;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;

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
public class RQCustomerEventDom {
    
    @NotBlank(message = "Account type is required")
    @Pattern(regexp = "^(SAVINGS|CHECKING|INVESTMENT)$", message = "Account type must be SAVINGS, CHECKING or INVESTMENT")
    String accountType;
    
    @NotNull(message = "Initial balance is required")
    @PositiveOrZero(message = "Initial balance must be zero or positive")
    @DecimalMin(value = "0.0", message = "Initial balance cannot be negative")
    Double initialBalance;
    
    @NotNull(message = "Customer ID is required")
    UUID customerId;
}
