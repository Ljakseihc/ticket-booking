package com.epam.nosql.migration.job.model.postgresql;

import com.epam.nosql.migration.job.model.Category;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Entity
@Table(name = "tickets")
@Getter
@Setter
public class TicketTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserTable user;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private EventTable event;

    @Column(name = "place", nullable = false)
    private Integer place;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private Category category;

    public TicketTable() {
    }

    public TicketTable(Long id, UserTable user, EventTable event, int place, Category category) {
        this.id = id;
        this.user = user;
        this.event = event;
        this.place = place;
        this.category = category;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TicketTable ticketTable = (TicketTable) o;
        return Objects.equals(id, ticketTable.id) && Objects.equals(user, ticketTable.user) && Objects.equals(event, ticketTable.event) && Objects.equals(place, ticketTable.place) && category == ticketTable.category;
    }

    public int hashCode() {
        return Objects.hash(id, user, event, place, category);
    }

    public String toString() {
        return "{" +
                "'id' : " + id +
                ", 'userId' : " + user.getId() +
                ", 'eventId' : " + event.getId() +
                ", 'place' : " + place +
                ", 'category' : '" + category +
                "'}";
    }
}
