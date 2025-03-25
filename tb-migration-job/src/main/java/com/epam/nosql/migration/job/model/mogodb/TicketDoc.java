package com.epam.nosql.migration.job.model.mogodb;

import com.epam.nosql.migration.job.model.Category;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Getter
@Setter
@Document(collection = "tickets")
public class TicketDoc {

    @Id
    private String id;
    private String userId;
    private String eventId;
    private Integer place;
    private Category category;

    public TicketDoc() {
    }

    public TicketDoc(String userId, String eventId, int place, Category category) {
        this.userId = userId;
        this.eventId = eventId;
        this.place = place;
        this.category = category;
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof TicketDoc ticket)) return false;

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

    @Override
    public String toString() {
        return "Ticket{" +
                "id='" + id + '\'' +
                ", user=" + userId +
                ", event='" + eventId + '\'' +
                ", place=" + place +
                ", category=" + category +
                '}';
    }
}
