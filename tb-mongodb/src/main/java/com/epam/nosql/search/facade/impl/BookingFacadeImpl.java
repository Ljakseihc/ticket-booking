package com.epam.nosql.search.facade.impl;

import com.epam.nosql.search.facade.BookingFacade;
import com.epam.nosql.search.model.Category;
import com.epam.nosql.search.model.dto.EventRequestDto;
import com.epam.nosql.search.model.dto.UserRequestDto;
import com.epam.nosql.search.model.entity.Event;
import com.epam.nosql.search.model.entity.Ticket;
import com.epam.nosql.search.model.entity.User;
import com.epam.nosql.search.service.EventService;
import com.epam.nosql.search.service.TicketService;
import com.epam.nosql.search.service.UserService;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class BookingFacadeImpl implements BookingFacade {

    private final EventService eventService;
    private final TicketService ticketService;
    private final UserService userService;

    public BookingFacadeImpl(EventService eventService, UserService userService, TicketService ticketService) {
        this.eventService = eventService;
        this.ticketService = ticketService;
        this.userService = userService;
    }

    @Override
    public Event getEventById(String eventId) {
        return eventService.getEventById(eventId);
    }

    @Override
    public List<Event> getEventsByTitle(String title, int pageSize, int pageNum) {
        return eventService.getEventsByTitle(title, pageSize, pageNum);
    }

    @Override
    public List<Event> getEventsForDay(LocalDate date, int pageSize, int pageNum) {
        return eventService.getEventsForDay(date, pageSize, pageNum);
    }

    @Override
    public Event createEvent(EventRequestDto eventDto) {
        return eventService.createEvent(eventDto);
    }

    @Override
    public Event updateEvent(EventRequestDto event) {
        return eventService.updateEvent(event);
    }

    @Override
    public void deleteEvent(String eventId) {
        eventService.deleteEvent(eventId);
    }

    @Override
    public User createUser(UserRequestDto user) {
        return userService.createUser(user);
    }

    @Override
    public User getUserById(String userId) {
        return userService.getUserById(userId);
    }

    @Override
    public User getUserByEmail(String email) {
        return userService.getUserByEmail(email);
    }

    @Override
    public List<User> getUsersByName(String name, int pageSize, int pageNum) {
        return userService.getUsersByName(name, pageSize, pageNum);
    }

    @Override
    public User updateUser(UserRequestDto user) {
        return userService.updateUser(user);
    }

    @Override
    public void deleteUser(String userId) {
        userService.deleteUser(userId);
    }

    @Override
    public Ticket bookTicket(String userId, String eventId, int place, Category category) {
        return ticketService.bookTicket(userId, eventId, place, category);
    }

    @Override
    public List<Ticket> getBookedTicketsByUserId(String userId, int pageSize, int pageNum) {
        return ticketService.getBookedTicketsByUserId(userId, pageSize, pageNum);
    }

    @Override
    public List<Ticket> getBookedTicketsByEventId(String eventId, int pageSize, int pageNum) {
        return ticketService.getBookedTicketsByEventId(eventId, pageSize, pageNum);
    }

    @Override
    public void cancelTicket(String ticketId) {
        ticketService.cancelTicket(ticketId);
    }
}
