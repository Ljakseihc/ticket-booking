package com.epam.nosql.migration.job.model;

import java.math.BigDecimal;
import java.util.List;

public record UserSaveDto(Long id, String name, String email, List<String> ticketIds, BigDecimal money) {
}
