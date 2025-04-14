package com.bank.movement.infrastructure.input.adapter.rest.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.bank.movement.domain.RSReportAccountDom;
import com.bank.movement.domain.RSReportDom;
import com.bank.movement.domain.RSReportMovementDom;
import com.bank.movement.infrastructure.input.adapter.rest.model.RSReport;
import com.bank.movement.infrastructure.input.adapter.rest.model.RSReportAccount;
import com.bank.movement.infrastructure.input.adapter.rest.model.RSReportMovement;

@Mapper(componentModel = "spring")
public interface ReportMapper {
    ReportMapper INSTANCE = Mappers.getMapper(ReportMapper.class);

    // Mapping for RSReport
    RSReport toReport(RSReportDom dom);
    RSReportDom toDom(RSReport report);
    
    // Mapping for RSReportAccount
    RSReportAccount toReportAccount(RSReportAccountDom dom);
    RSReportAccountDom toDom(RSReportAccount reportAccount);
    
    // Mapping for RSReportMovement
    RSReportMovement toReportMovement(RSReportMovementDom dom);
    RSReportMovementDom toDom(RSReportMovement reportMovement);
}