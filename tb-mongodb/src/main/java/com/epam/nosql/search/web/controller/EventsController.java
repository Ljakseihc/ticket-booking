package com.epam.nosql.search.web.controller;

import com.epam.nosql.search.facade.impl.BookingFacadeImpl;
import com.epam.nosql.search.model.dto.EventRequestDto;
import com.epam.nosql.search.model.entity.Event;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static com.epam.nosql.search.util.Constants.DATE_FORMAT_REGEXP;
import static com.epam.nosql.search.util.Constants.DATE_TIME_FORMATTER;

@Slf4j
@Validated
@Controller
@RequestMapping("/events")
public class EventsController {

    private final BookingFacadeImpl bookingFacade;

    @Autowired
    public EventsController(BookingFacadeImpl bookingFacade) {
        this.bookingFacade = bookingFacade;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> showEventById(
            @NotNull(message = "Id is not provided") @PathVariable String id) {
        log.info("Showing event by id: {}", id);
        Event eventById = bookingFacade.getEventById(id);
        log.info("Event by id: {} successfully found", id);
        return ResponseEntity.ok(Map.of("event", eventById));
    }

    @GetMapping("/title/{title}")
    public ResponseEntity<Map<String, Object>> showEventsByTitle(
            @NotNull(message = "Title is not provided") @PathVariable String title,
            @NotNull(message = "pageSize is not provided") @RequestParam int pageSize,
            @NotNull(message = "pageNum is not provided") @RequestParam int pageNum) {
        log.info("Showing events by title: {}", title);
        List<Event> eventsByTitle = bookingFacade.getEventsByTitle(title, pageSize, pageNum);
        if (eventsByTitle.isEmpty()) {
            log.info("Can not to get events by title: {}", title);
            throw new NoSuchElementException("Can not to find events by title: " + title);
        } else {
            log.info("Events by title '{}' successfully found", title);
            return ResponseEntity.ok(Map.of("events", eventsByTitle));
        }

    }

    @GetMapping("/day/{day}")
    public ResponseEntity<Map<String, Object>> showEventsForDay(
            @NotNull(message = "Day is not provided") @Pattern(regexp = DATE_FORMAT_REGEXP) @PathVariable String day,
            @NotNull(message = "pageSize is not provided") @RequestParam int pageSize,
            @NotNull(message = "pageNum is not provided") @RequestParam int pageNum) {
        log.info("Showing events for day: {}", day);
        LocalDate date = parseFromStringToDate(day);
        List<Event> eventsForDay = bookingFacade.getEventsForDay(date, pageSize, pageNum);
        if (eventsForDay.isEmpty()) {
            log.info("Can not to get events for day: {}", day);
            throw new NoSuchElementException("Can not to find events by day: " + day);
        } else {
            log.info("Events for day: {} successfully found", day);
        }
        return ResponseEntity.ok(Map.of("events", eventsForDay));
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createEvent(
            @NotNull(message = "Day is not provided") @RequestParam String title,
            @NotNull(message = "Day is not provided") @Pattern(regexp = DATE_FORMAT_REGEXP) @RequestParam String day,
            @NotNull(message = "Day is not provided") @RequestParam BigDecimal price) {
        log.info("Creating an event with title={} and day={} and price={}", title, day, price);
        Event event = bookingFacade.createEvent(new EventRequestDto(
                title,
                parseFromStringToDate(day),
                price)
        );
        log.info("The event successfully created");
        return ResponseEntity.ok(Map.of("event", event));
    }

    @PutMapping
    public ResponseEntity<Map<String, Object>> updateEvent(
            @NotNull(message = "Id is not provided") @RequestParam String id,
            @NotNull(message = "Title is not provided") @RequestParam String title,
            @NotNull(message = "Day is not provided") @RequestParam String day,
            @NotNull(message = "Price is not provided") @RequestParam BigDecimal price) {
        log.info("Updating an event with id: {}", id);
        Event event = bookingFacade.updateEvent(new EventRequestDto(
                id,
                title,
                parseFromStringToDate(day),
                price)
        );
        log.info("The event with id: {} successfully updated", id);
        return ResponseEntity.ok(Map.of("event", event));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteEvent(
            @NotNull(message = "Id is not provided") @PathVariable String id) {
        log.info("Deleting an event with id: {}", id);
        bookingFacade.deleteEvent(id);
        log.info("The event with id: {} successfully deleted", id);
        return ResponseEntity.ok(Map.of("events", id));
    }

    private LocalDate parseFromStringToDate(String date) {
        return LocalDate.parse(date, DATE_TIME_FORMATTER);
    }
}
