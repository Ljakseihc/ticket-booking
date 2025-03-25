package com.epam.nosql.search.model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record EventRequestDto(String id, String title, LocalDate date, BigDecimal ticketPrice) {
    public EventRequestDto(String id, String title, LocalDate date, BigDecimal ticketPrice) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.ticketPrice = ticketPrice;
    }

    public EventRequestDto(String title, LocalDate date, BigDecimal ticketPrice) {
        this("", title, date, ticketPrice);
    }

    public EventRequestDto(String id, String title, LocalDate date) {
        this(id, title, date, null);
    }

    public EventRequestDto(LocalDate date, String title) {
        this("", title, date, null);
    }
}
