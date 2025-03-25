package com.epam.nosql.search.facade;

import com.epam.nosql.search.model.Category;
import com.epam.nosql.search.model.dto.EventRequestDto;
import com.epam.nosql.search.model.dto.UserRequestDto;
import com.epam.nosql.search.model.entity.Event;
import com.epam.nosql.search.model.entity.Ticket;
import com.epam.nosql.search.model.entity.User;

import java.time.LocalDate;
import java.util.List;

public interface BookingFacade {

    User getUserById(String id);

    User getUserByEmail(String email);

    List<User> getUsersByName(String name, int pageSize, int pageNum);

    User createUser(UserRequestDto user);

    User updateUser(UserRequestDto user);

    void deleteUser(String userId);

    Event getEventById(String eventId);

    List<Event> getEventsByTitle(String title, int pageSize, int pageNum);

    List<Event> getEventsForDay(LocalDate day, int pageSize, int pageNum);

    Event createEvent(EventRequestDto event);

    Event updateEvent(EventRequestDto event);

    void deleteEvent(String eventId);

    Ticket bookTicket(String userId, String eventId, int place, Category category);

    List<Ticket> getBookedTicketsByUserId(String userId, int pageSize, int pageNum);

    List<Ticket> getBookedTicketsByEventId(String eventId, int pageSize, int pageNum);

    void cancelTicket(String ticketId);
}
