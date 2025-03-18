package com.epam.nosql.search.service;

import com.epam.nosql.search.dto.Category;
import com.epam.nosql.search.dto.entity.Event;
import com.epam.nosql.search.dto.entity.Ticket;
import com.epam.nosql.search.dto.entity.User;

import java.util.List;

public interface TicketService {

    Ticket bookTicket(long userId, long eventId, int place, Category category);
    List<Ticket> getBookedTickets(User user, int pageSize, int pageNum);
    List<Ticket> getBookedTickets(Event event, int pageSize, int pageNum);
    boolean cancelTicket(long ticketId);
}