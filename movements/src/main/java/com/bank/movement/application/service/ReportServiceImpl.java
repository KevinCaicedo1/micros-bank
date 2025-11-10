package com.bank.movement.application.service;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.bank.movement.application.output.port.CustomerClientPort;
import com.bank.movement.application.input.port.ReportService;
import com.bank.movement.application.mapper.PGReportMapper;
import com.bank.movement.application.output.port.RepositoryAccountPort;
import com.bank.movement.application.output.port.RepositoryMovementPort;
import com.bank.movement.domain.RSReportAccountDom;
import com.bank.movement.domain.RSReportDom;
import com.bank.movement.infrastructure.exception.CustomException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final CustomerClientPort customerServiceClient;
    private final RepositoryAccountPort repositoryAccountPort;
    private final RepositoryMovementPort repositoryMovementPort;
    private final PGReportMapper reportMapper;

    @Override
    public Mono<RSReportDom> generateReportByCustomerId(UUID customerId, LocalDate startDate, LocalDate endDate) {
        log.info("Generating report for customerId: {}", customerId);

        return customerServiceClient.getCustomerById(customerId)
            .flatMap(customer -> repositoryAccountPort.findByClientId(customerId)
                .flatMap(accountDom -> {
                    RSReportAccountDom reportAccountDom = reportMapper.accountToDom(accountDom);
                    return repositoryMovementPort.findByClientIdAndDate(customerId, startDate, endDate)
                        .map(reportMapper::movementToDom)
                        .collectList()
                        .map(movements -> {
                            reportAccountDom.setMovements(movements);
                            return reportAccountDom;
                        });
                })
                .collectList()
                .map(accounts -> new RSReportDom(customer.getName(), customer.getIsActive(), customer.getIdentification(), accounts))
            )
            .doOnSuccess(report -> log.info("Successfully generated report for customerId: {}", customerId))
            .doOnError(error -> log.error("Error generating report: {}", error.getMessage()))
            .switchIfEmpty(Mono.error(new CustomException("Failed to generate report", HttpStatus.INTERNAL_SERVER_ERROR)));
    }
}
