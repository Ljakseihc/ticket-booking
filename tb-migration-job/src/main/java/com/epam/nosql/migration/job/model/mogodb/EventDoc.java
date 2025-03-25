package com.epam.nosql.migration.job.model.mogodb;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Document(collection = "events")
public class EventDoc {

    @Id
    private String id;
    private String title;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;
    private BigDecimal ticketPrice;
    private List<String> ticketIds;

    public EventDoc() {
    }

    public EventDoc(String title, LocalDate date, BigDecimal ticketPrice, List<String> ticketIds) {
        this.title = title;
        this.date = date;
        this.ticketPrice = ticketPrice;
        this.ticketIds = ticketIds;
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof EventDoc eventDoc)) return false;

        return Objects.equals(id, eventDoc.id) && Objects.equals(title, eventDoc.title) && Objects.equals(date, eventDoc.date) && Objects.equals(ticketPrice, eventDoc.ticketPrice) && Objects.equals(ticketIds, eventDoc.ticketIds);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(id);
        result = 31 * result + Objects.hashCode(title);
        result = 31 * result + Objects.hashCode(date);
        result = 31 * result + Objects.hashCode(ticketPrice);
        result = 31 * result + Objects.hashCode(ticketIds);
        return result;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", date=" + date +
                ", ticketPrice=" + ticketPrice +
                ", tickets=" + ticketIds +
                '}';
    }
}
