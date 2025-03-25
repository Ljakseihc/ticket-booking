package com.epam.nosql.search.service;


import com.epam.nosql.search.model.dto.EventRequestDto;
import com.epam.nosql.search.model.entity.Event;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface EventService {

    Event getEventById(String eventId);

    List<Event> getEventsByTitle(String title, int pageSize, int pageNum);

    List<Event> getEventsForDay(LocalDate day, int pageSize, int pageNum);

    Event createEvent(EventRequestDto eventDto);

    Event updateEvent(EventRequestDto eventDto);

    void deleteEvent(String eventId);
}
