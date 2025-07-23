package com.bank.customer.utils;

import com.bank.customer.domain.CustomerDom;
import com.bank.customer.infrastructure.output.repository.entity.CustomerEntity;

import java.util.UUID;

public class MockCustomerDataUtils {
    public static final String CUSTOMER_ID = "123e4567-e89b-12d3-a456-426614174000";
    public static final String IDENTIFICATION = "987654321";
    public static final String NAME = "John Doe";
    public static final String GENDER = "Male";
    public static final String ADDRESS = "123 Main St";
    public static final String PHONE = "555-1234";
    public static final String PASSWORD = "securePassword";
    public static final Boolean IS_ACTIVE = true;

    public static CustomerDom getCustomerDom() {
        CustomerDom dom = new CustomerDom();
        dom.setCustomerId(CUSTOMER_ID);
        dom.setName(NAME);
        dom.setGender(GENDER);
        dom.setIdentification(IDENTIFICATION);
        dom.setAddress(ADDRESS);
        dom.setPhone(PHONE);
        dom.setPassword(PASSWORD);
        dom.setIsActive(IS_ACTIVE);
        return dom;
    }

    public static CustomerEntity getCustomerEntity() {
        CustomerEntity entity = new CustomerEntity();
        entity.setCustomerId(UUID.fromString(CUSTOMER_ID));
        entity.setPersonName(NAME);
        entity.setGender(GENDER);
        entity.setIdentification(IDENTIFICATION);
        entity.setPersonAddress(ADDRESS);
        entity.setPhone(PHONE);
        entity.setCustomerPassword(PASSWORD);
        entity.setCustomerStatus(IS_ACTIVE);
        return entity;
    }
}
