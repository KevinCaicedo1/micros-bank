package com.bank.movement.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.bank.movement.domain.RSAccountDom;
import com.bank.movement.domain.RSMovementDom;
import com.bank.movement.domain.RSReportAccountDom;
import com.bank.movement.domain.RSReportMovementDom;

@Mapper(componentModel = "spring")
public interface PGReportMapper {

    @Mapping(target = "movements", ignore = true) // If movements need to be mapped explicitly, provide specific mappings or a method.
    RSReportAccountDom accountToDom(RSAccountDom accountDom);

    @Mapping(source = "movementValue", target = "amount")
    @Mapping(source = "createDate", target = "movementDate")
    RSReportMovementDom movementToDom(RSMovementDom movementDom);
}
