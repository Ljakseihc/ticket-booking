package com.epam.nosql.search.service.impl;

import com.epam.nosql.search.model.Category;
import com.epam.nosql.search.model.aggregations.TicketAggregated;
import com.epam.nosql.search.model.aggregations.TicketBookingAggregation;
import com.epam.nosql.search.model.entity.Event;
import com.epam.nosql.search.model.entity.Ticket;
import com.epam.nosql.search.model.entity.User;
import com.epam.nosql.search.model.exceptions.NotFoundDocumentException;
import com.epam.nosql.search.repository.EventRepository;
import com.epam.nosql.search.repository.TicketRepository;
import com.epam.nosql.search.repository.UserRepository;
import com.epam.nosql.search.service.TicketService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final MongoTemplate mongoTemplate;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public TicketServiceImpl(TicketRepository ticketRepository, MongoTemplate mongoTemplate, EventRepository eventRepository, UserRepository userRepository) {
        this.ticketRepository = ticketRepository;
        this.mongoTemplate = mongoTemplate;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }


    @Override
    public Ticket bookTicket(String userId, String eventId, int place, Category category) {
        var ticketBookingAggregation = getUserEventDetails(userId, eventId)
                .orElseThrow(() -> new NotFoundDocumentException("Not found user or event"));

        if(Objects.isNull(ticketBookingAggregation.getUser()) || Objects.isNull(ticketBookingAggregation.getEvent())){
            throw new NotFoundDocumentException("Not found user or event");
        }

        throwRuntimeExceptionIfTicketAlreadyBooked(eventId, place, category);

        User user = ticketBookingAggregation.getUser();
        Event event = ticketBookingAggregation.getEvent();

        throwRuntimeExceptionIfUserNotHaveEnoughMoney(user, event);
        buyTicket(user, event);
        Ticket ticket = saveBookedTicket(userId, eventId, place, category);
        addTicketToUser(user, ticket);
        addTicketToEvent(event, ticket);
        log.info("Successfully booking of the ticket: {}", ticket);
        return ticket;
    }

    public Optional<TicketBookingAggregation> getUserEventDetails(String userId, String eventId) {
        LookupOperation lookupOperation = LookupOperation.newLookup()
                .from("events")
                .pipeline(Aggregation.match(Criteria.where("_id").is(eventId)))
                .as("eventDetails");

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("_id").is(userId)),
                lookupOperation,
                Aggregation.unwind("eventDetails", true),
                Aggregation.project()
                        .and("eventDetails").as("event")
                        .and("_id").as("user._id")
                        .and("name").as("user.name")
                        .and("email").as("user.email")
                        .and("ticketIds").as("user.ticketIds")
                        .and("money").as("user.money")
        );

        AggregationResults<TicketBookingAggregation> results = mongoTemplate.aggregate(aggregation, "users", TicketBookingAggregation.class);
        return Optional.ofNullable(results.getUniqueMappedResult());
    }

    private void addTicketToEvent(Event event, Ticket ticket) {
        var ticketIds = event.getTicketIds();
        ticketIds.add(ticket.getId());
        event.setTicketIds(ticketIds);
        eventRepository.save(event);
    }

    private void addTicketToUser(User user, Ticket ticket) {
        var ticketIds = user.getTicketIds();
        ticketIds.add(ticket.getId());
        user.setTicketIds(ticketIds);
        userRepository.save(user);
    }

    private Ticket saveBookedTicket(String userId, String eventId, int place, Category category) {
        return ticketRepository.save(new Ticket(userId, eventId, place, category));
    }

    private void buyTicket(User user, Event event) {
        user.setMoney(subtractTicketPriceFromUserMoney(user, event));
        userRepository.save(user);
    }

    private BigDecimal subtractTicketPriceFromUserMoney(User user, Event event) {
        return user.getMoney().subtract(event.getTicketPrice());
    }

    private void throwRuntimeExceptionIfUserNotHaveEnoughMoney(User user, Event event) {
        if (Objects.isNull(user.getMoney()) || !userHasEnoughMoneyForTicket(user.getMoney(), event)) {
            throw new NotFoundDocumentException(
                    "The user with id " + user.getId() +
                            " does not have enough money for ticket with event id " + event.getId()
            );
        }
    }

    private boolean userHasEnoughMoneyForTicket(BigDecimal money, Event event) {
        return money.compareTo(event.getTicketPrice()) > -1;
    }

    private void throwRuntimeExceptionIfTicketAlreadyBooked(String eventId, int place, Category category) {
        if (ticketRepository.existsByEventIdAndPlaceAndCategory(eventId, place, category)) {
            throw new RuntimeException("This ticket already booked");
        }
    }

    @Override
    public List<Ticket> getBookedTicketsByUserId(@NonNull String userId, int pageSize, int pageNum) {
        log.info("Finding all booked tickets by user {} with page size {} and number of page {}",
                userId, pageSize, pageNum);
        Page<Ticket> ticketsByUser = ticketRepository.findByUserId(userId,
                PageRequest.of(pageNum - 1, pageSize));
        if (!ticketsByUser.hasContent()) {
            log.warn("Can not to fina a list of booked tickets by user with id: {}", userId);
            return List.of();
        }
        log.info("All booked tickets successfully found by user {} with page size {} and number of page {}",
                userId, pageSize, pageNum);
        return ticketsByUser.getContent();
    }

    @Override
    public List<Ticket> getBookedTicketsByEventId(@NonNull String eventId, int pageSize, int pageNum) {
        log.info("Finding all booked tickets by event {} with page size {} and number of page {}",
                eventId, pageSize, pageNum);
        Page<Ticket> ticketsByEvent = ticketRepository.findByEventId(
                eventId,
                PageRequest.of(pageNum - 1, pageSize)
        );
        if (!ticketsByEvent.hasContent()) {
            log.warn("Can not to fina a list of booked tickets by event with id: {}", eventId);
            return List.of();
        }
        log.info("All booked tickets successfully found by event {} with page size {} and number of page {}",
                eventId, pageSize, pageNum);
        return ticketsByEvent.getContent();
    }

    private Ticket getTicketById(String ticketId){
        return ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Can not to find an ticket by id: " + ticketId));
    }

    @Override
//    @Transactional
    public void cancelTicket(String ticketId) {
        log.info("Start canceling a ticket with id: {}", ticketId);
        TicketAggregated aggregatedTicket = aggregateTicketToCancel(ticketId)
                .orElseThrow(() -> new NotFoundDocumentException("Do not for ticket with id: " + ticketId));

        if(aggregatedTicket.getUser() == null || aggregatedTicket.getEvent() == null) {
            throw new NotFoundDocumentException("User and|or event did not found in ticket with id: " + ticketId);
        }

        ticketRepository.deleteById(ticketId);
        removeTicketFromUser(aggregatedTicket.getUser(), ticketId);
        removeTicketFromEvent(aggregatedTicket.getEvent(), ticketId);
        log.info("Successfully canceling of the ticket with id: {}", ticketId);
    }


    private void removeTicketFromEvent(Event event, String ticketId) {
        List<String> ticketIds = event.getTicketIds();
        ticketIds.remove(ticketId);
        event.setTicketIds(ticketIds);
        eventRepository.save(event);
    }

    private void removeTicketFromUser(User user, String ticketId) {
        List<String> ticketIds = user.getTicketIds();
        ticketIds.remove(ticketId);
        user.setTicketIds(ticketIds);
        userRepository.save(user);
    }

    private Optional<TicketAggregated> aggregateTicketToCancel(String ticketId) {
        MatchOperation matchOperation = Aggregation.match(Criteria.where("_id").is(new ObjectId(ticketId)));

        Aggregation aggregation = Aggregation.newAggregation(
                matchOperation,
                Aggregation.addFields().addField("userIdObjectId").withValue(ConvertOperators.ToObjectId.toObjectId("$userId")).build(),
                Aggregation.addFields().addField("eventIdObjectId").withValue(ConvertOperators.ToObjectId.toObjectId("$eventId")).build(),
                LookupOperation.newLookup()
                        .from("users")
                        .localField("userIdObjectId")
                        .foreignField("_id")
                        .as("user"),
                Aggregation.unwind("user"),
                LookupOperation.newLookup()
                        .from("events")
                        .localField("eventIdObjectId")
                        .foreignField("_id")
                        .as("event"),
                Aggregation.unwind("event"),
                Aggregation.project("_id")
                        .and("user._id").as("user._id")
                        .and("user.name").as("user.name")
                        .and("user.email").as("user.email")
                        .and("user.ticketIds").as("user.ticketIds")
                        .and("event._id").as("event._id")
                        .and("event.title").as("event.title")
                        .and("event.ticketPrice").as("event.ticketPrice")
                        .and("event.ticketIds").as("event.ticketIds")
                        .and("event.date").as("event.date")
                        .and("user._class").as("user._class")
                        .and("eventId").as("eventId")
                        .and("place").as("place")
                        .and("category").as("category")
                        .and("_class").as("_class")
        );

        AggregationResults<TicketAggregated> results = mongoTemplate.aggregate(aggregation, "tickets", TicketAggregated.class);
        return Optional.ofNullable(results.getUniqueMappedResult());
    }
}
