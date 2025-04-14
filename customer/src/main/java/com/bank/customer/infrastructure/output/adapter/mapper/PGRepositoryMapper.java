package com.bank.customer.infrastructure.output.adapter.mapper;

import com.bank.customer.domain.CustomerDom;
import com.bank.customer.infrastructure.output.repository.entity.CustomerEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface PGRepositoryMapper {

    @Mappings({
            @Mapping(source = "name", target = "personName"),
            @Mapping(source = "address", target = "personAddress"),
            @Mapping(source = "password", target = "customerPassword"),
            @Mapping(source = "isActive", target = "customerStatus")
    })
    CustomerEntity toEntity(CustomerDom customerDom);

    @Mappings({
            @Mapping(source = "personName", target = "name"),
            @Mapping(source = "personAddress", target = "address"),
            @Mapping(source = "customerPassword", target = "password"),
            @Mapping(source = "customerStatus", target = "isActive"),
            @Mapping(source = "customerId", target = "customerId"),
    })
    CustomerDom toDomain(CustomerEntity customerEntity);
}
