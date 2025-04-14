package com.bank.movement.application.service;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.bank.movement.application.input.port.CustomerServiceClient;
import com.bank.movement.application.input.port.ReportService;
import com.bank.movement.application.output.port.RepositoryAccountPort;
import com.bank.movement.application.output.port.RepositoryMovementPort;
import com.bank.movement.domain.RSReportAccountDom;
import com.bank.movement.domain.RSReportDom;
import com.bank.movement.infrastructure.exception.CustomException;
import com.bank.movement.infrastructure.output.adapter.mapper.PGRepositoryReportMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final CustomerServiceClient customerServiceClient;
    private final RepositoryAccountPort repositoryAccountPort;
    private final RepositoryMovementPort repositoryMovementPort;
    private final PGRepositoryReportMapper reportMapper;

    @Override
    public Mono<RSReportDom> generateReportByCustomerId(UUID customerId, LocalDate startDate, LocalDate endDate) {
        log.info("Generating report for customerId: {}", customerId);

        return customerServiceClient.getCustomerById(customerId)
            .flatMap(customer -> {
                return repositoryAccountPort.findByClientId(customerId)
                    .flatMap(accountEntity -> {
                        RSReportAccountDom accountDom = reportMapper.accountToDom(accountEntity);
                        return repositoryMovementPort.findByClientIdAndDate(customerId, startDate, endDate)
                            .map(reportMapper::movementToDom)
                            .collectList()
                            .map(movements -> {
                                accountDom.setMovements(movements);
                                return accountDom;
                            });
                    })
                    .collectList()
                    .map(accounts -> new RSReportDom(customer.getName(), customer.getIsActive(), customer.getIdentification(), accounts));
            })
            .doOnSuccess(report -> log.info("Successfully generated report for customerId: {}", customerId))
            .doOnError(error -> log.error("Error generating report: {}", error.getMessage()))
            .switchIfEmpty(Mono.error(new CustomException("Failed to generate report", HttpStatus.INTERNAL_SERVER_ERROR)));
    }

}
