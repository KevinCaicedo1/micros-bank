package com.bank.movement.infrastructure.input.adapter.rest.impl;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

import com.bank.movement.application.input.port.ReportService;
import com.bank.movement.infrastructure.input.adapter.rest.ReportsApi;
import com.bank.movement.infrastructure.input.adapter.rest.mapper.ReportMapper;
import com.bank.movement.infrastructure.input.adapter.rest.model.RSReport;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RestController
@AllArgsConstructor
@Slf4j
@Validated
@RequestMapping("/api/v1")
public class ReportController implements ReportsApi {

    private final ReportService reportService;
    private final ReportMapper reportMapper;

    @Override
    public Mono<ResponseEntity<RSReport>> reportsClientIdGet(String clientId, @NotNull @Valid LocalDate startDate,
            @NotNull @Valid LocalDate endDate, ServerWebExchange exchange) {

        log.info("Generating report for clientId: {} from {} to {}", clientId, startDate, endDate);
        
        return reportService.generateReportByCustomerId(UUID.fromString(clientId), startDate, endDate)
            .map(reportMapper::toReport)
            .map(ResponseEntity::ok)
            .doOnSuccess(report -> log.info("Successfully generated report for clientId: {}", clientId))
            .doOnError(error -> log.error("Error generating report for clientId {}: {}", clientId, error.getMessage()));
    }
}
