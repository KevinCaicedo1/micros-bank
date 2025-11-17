package com.bank.movement.domain;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

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
public class RQCreateMovementDom {
    
    @NotBlank(message = "Movement type is required")
    @Pattern(regexp = "^(DEPOSIT|WITHDRAWAL)$", message = "Movement type must be DEPOSIT or WITHDRAWAL")
    String movementType;
    
    @NotNull(message = "Movement value is required")
    @Positive(message = "Movement value must be greater than zero")
    @DecimalMin(value = "0.01", message = "Movement value must be at least 0.01")
    Double movementValue;
    
    @NotBlank(message = "Account number is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Account number must be 10 digits")
    String accountNumber;
}
