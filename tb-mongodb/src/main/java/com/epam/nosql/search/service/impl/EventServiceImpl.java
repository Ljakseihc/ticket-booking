package com.epam.nosql.search.service.impl;

import com.epam.nosql.search.model.dto.EventRequestDto;
import com.epam.nosql.search.model.entity.Event;
import com.epam.nosql.search.model.exceptions.ConflictWithExistingDataException;
import com.epam.nosql.search.repository.EventRepository;
import com.epam.nosql.search.service.EventService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    @Autowired
    public EventServiceImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public Event getEventById(@NonNull String eventId) {
        log.info("Finding an event by id: {}", eventId);
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Can not to find an event by id: " + eventId));
        log.info("Event with id {} successfully found ", eventId);
        return event;
    }

    @Override
    public List<Event> getEventsByTitle(@NonNull String title, int pageSize, int pageNum) {
        log.info("Finding all events by title {} with page size {} and number of page {}",
                title, pageSize, pageNum);
        Page<Event> eventsByTitle = eventRepository.findByTitle(title, PageRequest.of(pageNum - 1, pageSize));
        if (!eventsByTitle.hasContent()) {
            log.warn("Can not to find a list of events by title: {}", title);
            return List.of();
        }
        log.info("All events successfully found by title {} with page size {} and number of page {}",
                title, pageSize, pageNum);
        return eventsByTitle.getContent();
    }

    @Override
    public List<Event> getEventsForDay(@NonNull LocalDate date, int pageSize, int pageNum) {
        log.info("Finding all events for day {} with page size {} and number of page {}",
                date, pageSize, pageNum);
        Page<Event> eventsByTitle = eventRepository.findByDate(date, PageRequest.of(pageNum - 1, pageSize));
        if (!eventsByTitle.hasContent()) {
            log.warn("Can not to find a list of events for day: {}", date);
            return List.of();
        }
        log.info("All events successfully found for day {} with page size {} and number of page {}",
                date, pageSize, pageNum);

        return eventsByTitle.getContent();
    }

    @Override
    public Event createEvent(@NonNull EventRequestDto eventDto) {
        log.info("Start creating an event: {}", eventDto);
        eventExistsByTitleAndDay(eventDto.title(), eventDto.date());
        Event event = eventRepository.save(new Event(
                eventDto.title(),
                eventDto.date(),
                eventDto.ticketPrice()
        ));
        log.info("Successfully creation of the event: {}", eventDto);
        return event;
    }

    @Override
    public Event updateEvent(@NonNull EventRequestDto eventDto) {
        log.info("Start updating an event: {}", eventDto);

        eventExistsById(eventDto.id());
        eventExistsByTitleAndDay(eventDto.title(), eventDto.date());

        Event event = getEventById(eventDto.id());
        event.setTitle(eventDto.title());
        event.setDate(eventDto.date());
        event.setTicketPrice(eventDto.ticketPrice());

        Event updatedEvent = eventRepository.save(event);
        log.info("Successfully updating of the event: {}", eventDto);
        return updatedEvent;
    }

    @Override
    public void deleteEvent(@NonNull String eventId) {
        log.info("Start deleting an event with id: {}", eventId);
        eventRepository.deleteById(eventId);
        log.info("Successfully deletion of the event with id: {}", eventId);
    }

    private void eventExistsByTitleAndDay(String title, LocalDate date) {
        if(eventRepository.existsByTitleAndDate(title, date)){
            throw new ConflictWithExistingDataException("These title and day are already exists for one event");
        }
    }

    private void eventExistsById(String id) {
        if(eventRepository.existsById(id)){
            throw new ConflictWithExistingDataException("This event does not exist");
        }
    }
}