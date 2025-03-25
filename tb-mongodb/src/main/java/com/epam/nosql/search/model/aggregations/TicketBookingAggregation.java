package com.epam.nosql.search.model.aggregations;

import com.epam.nosql.search.model.entity.Event;
import com.epam.nosql.search.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TicketBookingAggregation {
    private String id;
    private User user;
    private Event event;
}
