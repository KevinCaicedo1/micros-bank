package com.bank.movement.infrastructure.output.adapter.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.bank.movement.domain.RSAccountDom;
import com.bank.movement.infrastructure.output.repository.entity.AccountEntity;
@Mapper(componentModel = "spring")
public interface PGRepositoryAccountMapper {
    @Mapping(target = "accountId", ignore = true)
    AccountEntity toEntity(RSAccountDom dom);

    RSAccountDom toDom(AccountEntity entity);
}
