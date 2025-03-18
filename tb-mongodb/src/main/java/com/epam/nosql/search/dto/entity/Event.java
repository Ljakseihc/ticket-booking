package com.epam.nosql.search.dto.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Document(collection = "event")
public class Event {

    @Id
    private String id;
    private String title;
    private Date date;
    private BigDecimal ticketPrice;
    private List<String> tickets;

    public Event() {
    }

    public Event(String title, Date date, BigDecimal ticketPrice) {
        this.title = title;
        this.date = date;
        this.ticketPrice = ticketPrice;
    }

    public Event(String id, String title, Date date, BigDecimal ticketPrice) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.ticketPrice = ticketPrice;
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Event event)) return false;

        return Objects.equals(id, event.id) && Objects.equals(title, event.title) && Objects.equals(date, event.date) && Objects.equals(ticketPrice, event.ticketPrice) && Objects.equals(tickets, event.tickets);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(id);
        result = 31 * result + Objects.hashCode(title);
        result = 31 * result + Objects.hashCode(date);
        result = 31 * result + Objects.hashCode(ticketPrice);
        result = 31 * result + Objects.hashCode(tickets);
        return result;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", date=" + date +
                ", ticketPrice=" + ticketPrice +
                ", tickets=" + tickets +
                '}';
    }
}
