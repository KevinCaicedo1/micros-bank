package com.bank.customer.infrastructure.output.repository.entity;
/*
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),  -- Unique Identifier for Client
    person_id UUID REFERENCES Person(id) ON DELETE CASCADE, -- Foreign key reference to Person
    client_password VARCHAR(100) NOT NULL,
    client_status BOOLEAN NOT NULL
 */

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "customer")
@PrimaryKeyJoinColumn(name = "person_id")
public class CustomerEntity extends PersonEntity{

    @Column(updatable = false, nullable = false, name = "customer_id", columnDefinition = "UUID")
    private UUID customerId;

    @Column(name = "customer_password", columnDefinition = "VARCHAR(100)", nullable = false)
    private String customerPassword;

    @Column(name = "customer_status", columnDefinition = "BOOLEAN", nullable = false)
    private Boolean customerStatus;

}
