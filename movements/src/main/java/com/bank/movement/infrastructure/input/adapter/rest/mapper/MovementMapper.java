package com.bank.movement.infrastructure.input.adapter.rest.mapper;

import com.bank.movement.domain.RQCreateMovementDom;
import com.bank.movement.domain.RSMovementDom;
import com.bank.movement.infrastructure.input.adapter.rest.model.RQCreateMovement;
import com.bank.movement.infrastructure.input.adapter.rest.model.RSMovement;
import java.util.Date;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MovementMapper {

    MovementMapper INSTANCE = Mappers.getMapper(MovementMapper.class);

    @Mapping(source = "createDate", target = "createDate", qualifiedByName = "dateToOffsetDateTime")
    RSMovement toMovement(RSMovementDom dom);

    @Mapping(source = "createDate", target = "createDate", qualifiedByName = "offsetDateTimeToDate")
    RSMovementDom toDom(RSMovement movement);

    RQCreateMovement toCreateRequest(RQCreateMovementDom dom);
    RQCreateMovementDom toDom(RQCreateMovement request);

    @Named("dateToOffsetDateTime")
    default OffsetDateTime dateToOffsetDateTime(Date date) {
        return date.toInstant().atOffset(ZoneOffset.UTC);
    }

    @Named("offsetDateTimeToDate")
    default Date offsetDateTimeToDate(OffsetDateTime offsetDateTime) {
        return Date.from(offsetDateTime.toInstant());
    }
}
