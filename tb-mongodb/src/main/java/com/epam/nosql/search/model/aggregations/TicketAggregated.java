package com.epam.nosql.search.model.aggregations;

import com.epam.nosql.search.model.Category;
import com.epam.nosql.search.model.entity.Event;
import com.epam.nosql.search.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TicketAggregated {

    @Id
    private String id;
    private User user;
    private Event event;
    private Integer place;
    private Category category;
}
