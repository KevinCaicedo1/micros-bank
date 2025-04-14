package com.bank.movement.infrastructure.output.adapter.mapper;

import com.bank.movement.domain.RSMovementDom;
import com.bank.movement.infrastructure.output.repository.entity.MovementEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PGRepositoryMovementMapper {

        @Mapping(target = "account", ignore = true)
        MovementEntity toEntity(RSMovementDom dom);
    
        @Mapping(source = "account.accountNumber", target = "accountNumber")
        RSMovementDom toDom(MovementEntity entity);
}
