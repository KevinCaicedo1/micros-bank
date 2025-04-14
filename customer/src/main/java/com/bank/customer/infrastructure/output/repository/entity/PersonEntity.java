package com.bank.customer.infrastructure.output.repository.entity;

import java.util.UUID;
// se debe usar jpa para poder usar las anotaciones de spring

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import lombok.Data;
// persistencia de datos


@Data
@Entity
@Table(name = "person")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class PersonEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "UUID", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "person_name", columnDefinition = "VARCHAR(100)", nullable = false)
    private String personName;

    @Column(name = "gender", columnDefinition = "VARCHAR(10)", nullable = false)
    private String gender;

    @Column(name = "identification", columnDefinition = "VARCHAR(10)", nullable = false)
    private String identification;

    @Column(name = "person_address", columnDefinition = "VARCHAR(255)", nullable = false)
    private String personAddress;

    @Column(name = "phone", columnDefinition = "VARCHAR(15)", nullable = false)
    private String phone;
}
