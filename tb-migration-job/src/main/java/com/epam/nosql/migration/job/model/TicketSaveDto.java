package com.epam.nosql.migration.job.model;

public record TicketSaveDto(Long id, String user, String event, Integer place, Category category){
}
