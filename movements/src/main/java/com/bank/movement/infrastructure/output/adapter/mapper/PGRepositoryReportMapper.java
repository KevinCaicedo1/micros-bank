package com.bank.movement.infrastructure.output.adapter.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.bank.movement.domain.RSReportAccountDom;
import com.bank.movement.domain.RSReportMovementDom;
import com.bank.movement.infrastructure.output.repository.entity.AccountEntity;
import com.bank.movement.infrastructure.output.repository.entity.MovementEntity;

@Mapper(componentModel = "spring")
public interface PGRepositoryReportMapper {

    @Mapping(target = "movements", ignore = true) // If movements need to be mapped explicitly, provide specific mappings or a method.
    RSReportAccountDom accountToDom(AccountEntity accountEntity);

    @Mapping(source = "movementValue", target = "amount")
    @Mapping(source = "createDate", target = "movementDate")
    RSReportMovementDom movementToDom(MovementEntity movementEntity);
}
