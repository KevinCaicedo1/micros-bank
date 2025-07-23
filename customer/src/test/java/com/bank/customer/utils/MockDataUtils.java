package com.bank.customer.utils;

import java.util.UUID;

import com.bank.customer.domain.CustomerDom; import com.bank.customer.infrastructure.output.repository.entity.CustomerEntity;

public class MockDataUtils { public static final String CUSTOMER_ID = "123e4567-e89b-12d3-a456-426614174000"; public static final String IDENTIFICATION = "987654321"; public static final String NAME = "John Doe"; public static final String GENDER = "Male"; public static final String ADDRESS = "123 Main St"; public static final String PHONE = "555-1234"; public static final String PASSWORD = "securePassword"; public static final Boolean IS_ACTIVE = true;

public static CustomerDom getCustomerDom() {
    CustomerDom customerDom = new CustomerDom();
    customerDom.setCustomerId(CUSTOMER_ID);
    customerDom.setName(NAME);
    customerDom.setGender(GENDER);
    customerDom.setIdentification(IDENTIFICATION);
    customerDom.setAddress(ADDRESS);
    customerDom.setPhone(PHONE);
    customerDom.setPassword(PASSWORD);
    customerDom.setIsActive(IS_ACTIVE);
    return customerDom;
}

public static CustomerEntity getCustomerEntity() {
    CustomerEntity entity = new CustomerEntity();
    entity.setCustomerId(UUID.fromString(CUSTOMER_ID));
    entity.setPersonName(NAME);
    entity.setPersonAddress(ADDRESS);
    entity.setCustomerPassword(PASSWORD);
    entity.setCustomerStatus(IS_ACTIVE);
    // Completa otros campos si existen en tu entidad
    return entity;
}
}