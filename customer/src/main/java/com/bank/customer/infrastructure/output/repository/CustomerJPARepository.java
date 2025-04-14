package com.bank.customer.infrastructure.output.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bank.customer.infrastructure.output.repository.entity.CustomerEntity;

@Repository
public interface CustomerJPARepository extends JpaRepository<CustomerEntity, UUID> {
    CustomerEntity findByCustomerId(UUID customerId);
    CustomerEntity findByIdentification(String identification);
    Void deleteByCustomerId(UUID customerId);
}
