package com.bank.movement.infrastructure.input.adapter.rest.mapper;

import com.bank.movement.domain.RQCreateAccountDom;
import com.bank.movement.domain.RQUpdateAccountDom;
import com.bank.movement.domain.RSAccountDom;
import com.bank.movement.infrastructure.input.adapter.rest.model.RQCreateAccount;
import com.bank.movement.infrastructure.input.adapter.rest.model.RQUpdateAccount;
import com.bank.movement.infrastructure.input.adapter.rest.model.RSAccount;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

    // Mapping for RQCreateAccount
    RQCreateAccount toCreateRequest(RQCreateAccountDom dom);
    RQCreateAccountDom toDom(RQCreateAccount request);

    // Mapping for RQUpdateAccount
    RQUpdateAccount toUpdateRequest(RQUpdateAccountDom dom);
    RQUpdateAccountDom toDom(RQUpdateAccount request);

    // Mapping for RSAccount
    RSAccount toAccount(RSAccountDom dom);
    RSAccountDom toDom(RSAccount account);
}
