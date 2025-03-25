package com.epam.nosql.search.service;

import com.epam.nosql.search.model.Category;
import com.epam.nosql.search.model.entity.Event;
import com.epam.nosql.search.model.entity.Ticket;
import com.epam.nosql.search.model.entity.User;

import java.util.List;

public interface TicketService {

    Ticket bookTicket(String userId, String eventId, int place, Category category);

    List<Ticket> getBookedTicketsByUserId(String userId, int pageSize, int pageNum);

    List<Ticket> getBookedTicketsByEventId(String eventId, int pageSize, int pageNum);

    void cancelTicket(String ticketId);
}