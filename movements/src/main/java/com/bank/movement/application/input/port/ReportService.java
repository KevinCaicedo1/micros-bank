package com.bank.movement.application.input.port;

import java.time.LocalDate;
import java.util.UUID;

import com.bank.movement.domain.RSReportDom;

import reactor.core.publisher.Mono;

public interface ReportService {
    Mono<RSReportDom> generateReportByCustomerId(UUID customerId, LocalDate startDate, LocalDate endDate);
}
