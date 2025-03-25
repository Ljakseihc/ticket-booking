package com.epam.nosql.search.model.dto;

import com.epam.nosql.search.model.Category;

public record TicketRequestDto(String id, String userId, String eventId, Integer place, Category category) {
    public TicketRequestDto(String id, String userId, String eventId, Integer place, Category category) {
        this.id = id;
        this.userId = userId;
        this.eventId = eventId;
        this.place = place;
        this.category = category;
    }


}
