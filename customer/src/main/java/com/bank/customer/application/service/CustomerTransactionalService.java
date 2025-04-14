package com.bank.customer.application.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bank.customer.infrastructure.output.repository.CustomerJPARepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerTransactionalService {
    private final CustomerJPARepository customerRepository;

    @Transactional
    public void deleteCustomer(UUID id) {
        customerRepository.deleteById(id);
    }
}