package com.bank.customer.infrastructure.input.adapter.rest.mapper;

import com.bank.customer.domain.CustomerDom;
import com.bank.customer.infrastructure.input.adapter.rest.model.CustomerResponse;
import com.bank.customer.infrastructure.input.adapter.rest.model.RQCreateCustomer;
import com.bank.customer.infrastructure.input.adapter.rest.model.RQUpdateCustomer;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

    RQCreateCustomer toRQCreateCustomer(CustomerDom domain);

    RQUpdateCustomer toRQUpdateCustomer(CustomerDom domain);

    CustomerResponse toCustomerResponse(CustomerDom domain);

    // Opcionales si los necesitas en algún punto
    CustomerDom fromRQCreateCustomer(RQCreateCustomer dto);

    CustomerDom fromRQUpdateCustomer(RQUpdateCustomer dto);
}
