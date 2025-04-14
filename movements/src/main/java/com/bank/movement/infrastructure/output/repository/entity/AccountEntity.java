package com.bank.movement.infrastructure.output.repository.entity;
// mapeamos la tabla movements con jpa, tambien utilizamos lombok para generar los getters y setters
// y el constructor
/*
    account_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),  -- Unique Identifier for Account
    account_number VARCHAR(50) UNIQUE NOT NULL,
    account_type VARCHAR(50),
    initial_balance DECIMAL(10, 2) NOT NULL,
    account_status BOOLEAN NOT NULL,
    client_id UUID NOT NULL

 */
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "account")
public class AccountEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "account_id", nullable = false, unique = true)
    private UUID accountId;

    @Column(name = "account_number", nullable = false, unique = true)
    private String accountNumber;

    @Column(name = "account_type", nullable = false)
    private String accountType;

    @Column(name = "initial_balance", nullable = false)
    private Double initialBalance;

    @Column(name = "account_status", nullable = false)
    private Boolean accountStatus;

    @Column(name = "client_id", nullable = false)
    private UUID clientId;

    
}
