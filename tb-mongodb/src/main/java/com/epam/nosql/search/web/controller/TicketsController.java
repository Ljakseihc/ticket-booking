package com.epam.nosql.search.web.controller;

import com.epam.nosql.search.facade.impl.BookingFacadeImpl;
import com.epam.nosql.search.model.Category;
import com.epam.nosql.search.model.dto.TicketRequestDto;
import com.epam.nosql.search.model.entity.Event;
import com.epam.nosql.search.model.entity.Ticket;
import com.epam.nosql.search.model.entity.User;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Slf4j
@Validated
@Controller
@RequestMapping("/tickets")
public class TicketsController {

    private final BookingFacadeImpl bookingFacade;

    public TicketsController(BookingFacadeImpl bookingFacade) {
        this.bookingFacade = bookingFacade;
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Map<String, Object>> showTicketsByUser(
            @NotNull(message = "userId is not provided") @PathVariable String userId,
            @NotNull(message = "pageSize is not provided") @RequestParam int pageSize,
            @NotNull(message = "pageNum is not provided") @RequestParam int pageNum) {
        log.info("Showing the tickets by user with id: {}", userId);
        User user = bookingFacade.getUserById(userId);
        List<TicketRequestDto> bookedTickets = bookingFacade.getBookedTicketsByUserId(user.getId(), pageSize, pageNum).stream()
                .map(ticket -> new TicketRequestDto(
                        ticket.getId(),
                        ticket.getUserId(),
                        ticket.getEventId(),
                        ticket.getPlace(),
                        ticket.getCategory()
                ))
                .toList();
        if (bookedTickets.isEmpty()) {
            log.info("Can not to find the tickets by user with id: {}", userId);
            throw new NoSuchElementException("Can not to find the tickets with id: " + userId);
        }
        log.info("The tickets successfully found");

        return ResponseEntity.ok(Map.of("tickets", bookedTickets));
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<Map<String, Object>> showTicketsByEvent(
            @NotNull(message = "eventId is not provided") @PathVariable String eventId,
            @NotNull(message = "pageSize is not provided") @RequestParam int pageSize,
            @NotNull(message = "pageNum is not provided") @RequestParam int pageNum) {
        log.info("Showing the tickets by event with id: {}", eventId);
        Event event = bookingFacade.getEventById(eventId);
        List<Ticket> bookedTickets = bookingFacade.getBookedTicketsByEventId(event.getId(), pageSize, pageNum);
        if (bookedTickets.isEmpty()) {
            log.info("Can not to find the tickets by event with id: {}", eventId);
        } else {
            log.info("The tickets successfully found");
        }
        return ResponseEntity.ok(Map.of("tickets", bookedTickets));
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> bookTicket(
            @NotNull(message = "userId is not provided") @RequestParam String userId,
            @NotNull(message = "eventId is not provided") @RequestParam String eventId,
            @NotNull(message = "place is not provided") @RequestParam int place,
            @NotNull(message = "category is not provided") @RequestParam Category category) {
        log.info("Booking a ticket: userId={}, eventId={}, place={}, category={}", userId, eventId, place, category);
        Ticket ticket = bookingFacade.bookTicket(userId, eventId, place, category);
        log.info("The ticket successfully booked");
        return ResponseEntity.ok(Map.of("ticket", ticket));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> cancelTicket(
            @NotNull(message = "id is not provided") @PathVariable String id) {
        log.info("Canceling ticket with id: {}", id);
        bookingFacade.cancelTicket(id);
        log.info("The ticket with id: {} successfully canceled", id);
        return ResponseEntity.ok(Map.of("ticket", id));
    }
}
