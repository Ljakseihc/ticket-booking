package com.epam.nosql.search.model.entity;

import com.epam.nosql.search.model.Category;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "tickets")
public class Ticket {

    @Id
    private String id;
    private String userId;
    private String eventId;
    private Integer place;
    private Category category;

    public Ticket(String userId, String eventId, int place, Category category) {
        this.userId = userId;
        this.eventId = eventId;
        this.place = place;
        this.category = category;
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Ticket ticket)) return false;

        return Objects.equals(id, ticket.id) && Objects.equals(userId, ticket.userId) && Objects.equals(eventId, ticket.eventId) && Objects.equals(place, ticket.place) && category == ticket.category;
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(id);
        result = 31 * result + Objects.hashCode(userId);
        result = 31 * result + Objects.hashCode(eventId);
        result = 31 * result + Objects.hashCode(place);
        result = 31 * result + Objects.hashCode(category);
        return result;
    }
}
