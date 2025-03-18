package com.epam.nosql.search.dto.entity;


import com.epam.nosql.search.dto.Category;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Getter
@Setter
@Document(collection = "tickets")
public class Ticket {

    @Id
    private String id;
    private User user;
    private String event;
    private Integer place;
    private Category category;

    public Ticket() {
    }

    public Ticket(String id, User user, String event, int place, Category category) {
        this.id = id;
        this.user = user;
        this.event = event;
        this.place = place;
        this.category = category;
    }

    public Ticket(User user, String event, int place, Category category) {
        this.user = user;
        this.event = event;
        this.place = place;
        this.category = category;
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Ticket ticket)) return false;

        return Objects.equals(id, ticket.id) && Objects.equals(user, ticket.user) && Objects.equals(event, ticket.event) && Objects.equals(place, ticket.place) && category == ticket.category;
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(id);
        result = 31 * result + Objects.hashCode(user);
        result = 31 * result + Objects.hashCode(event);
        result = 31 * result + Objects.hashCode(place);
        result = 31 * result + Objects.hashCode(category);
        return result;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id='" + id + '\'' +
                ", user=" + user +
                ", event='" + event + '\'' +
                ", place=" + place +
                ", category=" + category +
                '}';
    }
}
