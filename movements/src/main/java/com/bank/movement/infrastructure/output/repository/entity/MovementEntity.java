package com.bank.movement.infrastructure.output.repository.entity;
// mapeamos la tabla movements con jpa, tambien utilizamos lombok para generar los getters y setters

// y el constructor
/*
    movement_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),  -- Unique Identifier for Movement
    account_id UUID REFERENCES Account(account_id) ON DELETE CASCADE,  -- Foreign key reference to Account
    create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    movement_type VARCHAR(10) CHECK (movement_type IN ('DEPOSIT', 'WITHDRAWAL')) NOT NULL,
    movement_value DECIMAL(10, 2) NOT NULL,
    initial_balance DECIMAL(10, 2) NOT NULL,
    available_balance DECIMAL(10, 2) NOT NULL
 */

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.util.Date;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "movement")
public class MovementEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "movement_id", nullable = false, unique = true)
    private UUID movementId;

    @CreationTimestamp
    @Column(name = "create_date", nullable = false)
    private Date createDate;

    @Column(name = "movement_type", nullable = false)
    private String movementType;

    @Column(name = "movement_value", nullable = false)
    private Double movementValue;

    @Column(name = "initial_balance", nullable = false)
    private Double initialBalance;

    @Column(name = "available_balance", nullable = false)
    private Double availableBalance;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private AccountEntity account;

}
