package com.epam.nosql.migration.job.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record EventSaveDto(Long id, String title, LocalDate date, BigDecimal ticketPrice, List<String> tickets) {
}
