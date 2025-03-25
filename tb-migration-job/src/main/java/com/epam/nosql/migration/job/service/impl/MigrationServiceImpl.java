package com.epam.nosql.migration.job.service.impl;

import com.epam.nosql.migration.job.model.EventSaveDto;
import com.epam.nosql.migration.job.model.TicketSaveDto;
import com.epam.nosql.migration.job.model.UserSaveDto;
import com.epam.nosql.migration.job.model.mogodb.EventDoc;
import com.epam.nosql.migration.job.model.mogodb.TicketDoc;
import com.epam.nosql.migration.job.model.mogodb.UserDoc;
import com.epam.nosql.migration.job.model.postgresql.EventTable;
import com.epam.nosql.migration.job.model.postgresql.TicketTable;
import com.epam.nosql.migration.job.model.postgresql.UserTable;
import com.epam.nosql.migration.job.repository.mongodb.EventDocRepository;
import com.epam.nosql.migration.job.repository.mongodb.TicketDocRepository;
import com.epam.nosql.migration.job.repository.mongodb.UserDocRepository;
import com.epam.nosql.migration.job.repository.postgres.EventTableRepository;
import com.epam.nosql.migration.job.repository.postgres.TicketTableRepository;
import com.epam.nosql.migration.job.repository.postgres.UserTableRepository;
import com.epam.nosql.migration.job.service.MigrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MigrationServiceImpl implements MigrationService {

    private final EventTableRepository eventTableRepository;
    private final TicketTableRepository ticketTableRepository;
    private final UserTableRepository userTableRepository;

    private final UserDocRepository userDocRepository;
    private final EventDocRepository eventDocRepository;
    private final TicketDocRepository ticketDocRepository;

    @Autowired
    public MigrationServiceImpl(EventTableRepository eventTableRepository, TicketTableRepository ticketTableRepository, UserTableRepository userTableRepository, UserDocRepository userDocRepository, EventDocRepository eventDocRepository, TicketDocRepository ticketDocRepository) {
        this.eventTableRepository = eventTableRepository;
        this.ticketTableRepository = ticketTableRepository;
        this.userTableRepository = userTableRepository;
        this.userDocRepository = userDocRepository;
        this.eventDocRepository = eventDocRepository;
        this.ticketDocRepository = ticketDocRepository;
    }

    private final Map<String, String> usersMappingKeys = new HashMap<>();
    private final Map<String, String> eventsMappingKeys = new HashMap<>();
    private final Map<String, String> ticketsMappingKeys = new HashMap<>();

    @Override
    public void migrateFromSQLToMongo() {
        migrateUser();
        migrateEvents();
        migrateTickets();
        updateTicketsIdInUsers();
        updateTicketsIdInEvents();
    }

    private void migrateUser() {
        List<UserTable> usersInTable = new ArrayList<>();
        var usersIterator = userTableRepository.findAll();
        usersIterator.iterator().forEachRemaining(usersInTable::add);

        List<UserSaveDto> usersInCollection = usersInTable.stream()
                .map(user -> new UserSaveDto(
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.getTicketTables().stream().map(ticket -> String.valueOf(ticket.getId())).toList(),
                        user.getUserAccount() == null ? null : user.getUserAccount().getMoney()

                ))
                .toList();
        usersInCollection.forEach(userSaveDto -> {
            UserDoc savedUser = userDocRepository.save(new UserDoc(
                    userSaveDto.name(),
                    userSaveDto.email(),
                    userSaveDto.ticketIds(),
                    userSaveDto.money()
            ));
            usersMappingKeys.put(String.valueOf(userSaveDto.id()), savedUser.getId());

        });
    }

    private void migrateEvents() {
        List<EventTable> eventsInTable = new ArrayList<>();
        var events = eventTableRepository.findAll();
        events.iterator().forEachRemaining(eventsInTable::add);

        List<EventSaveDto> eventsInCollection = eventsInTable.stream()
                        .map(event -> new EventSaveDto(
                                event.getId(),
                                event.getTitle(),
                                LocalDate.ofInstant(event.getDate().toInstant(), ZoneId.systemDefault()),
                                event.getTicketPrice(),
                                event.getTicketTables().stream().map(ticket ->String.valueOf(ticket.getId())).toList()
                        ))
                        .toList();
        eventsInCollection.forEach(eventSaveDto -> {
            var savedEvent = eventDocRepository.save(new EventDoc(
                    eventSaveDto.title(),
                    eventSaveDto.date(),
                    eventSaveDto.ticketPrice(),
                    eventSaveDto.tickets()
            ));
            eventsMappingKeys.put(String.valueOf(eventSaveDto.id()), savedEvent.getId());
        });
    }

    private void migrateTickets() {
        List<TicketTable> ticketsInTable = new ArrayList<>();
        var tickets = ticketTableRepository.findAll();
        tickets.iterator().forEachRemaining(ticketsInTable::add);
        List<TicketSaveDto> ticketsInCollection = ticketsInTable.stream()
                        .map(ticket -> new TicketSaveDto(
                                ticket.getId(),
                                usersMappingKeys.get(String.valueOf(ticket.getUser().getId())),
                                eventsMappingKeys.get(String.valueOf(ticket.getEvent().getId())),
                                ticket.getPlace(),
                                ticket.getCategory()
                        ))
                .toList();


        ticketsInCollection.forEach(ticketSaveDto -> {
            var savedTicket = ticketDocRepository.save(new TicketDoc(
                    ticketSaveDto.user(),
                    ticketSaveDto.event(),
                    ticketSaveDto.place(),
                    ticketSaveDto.category()
            ));

            ticketsMappingKeys.put(String.valueOf(ticketSaveDto.id()), savedTicket.getId());

        });
    }

    private void updateTicketsIdInUsers(){
        List<UserDoc> users = userDocRepository.findAll();
        for(UserDoc user: users) {
            user.setTicketIds(user.getTicketIds().stream()
                    .map(ticketsMappingKeys::get)
                    .toList());

            userDocRepository.save(user);
        }
    }

    private void updateTicketsIdInEvents(){
        List<EventDoc> events = eventDocRepository.findAll();
        for(EventDoc event: events) {
            event.setTicketIds(event.getTicketIds().stream()
                    .map(ticketsMappingKeys::get)
                    .toList());

            eventDocRepository.save(event);
        }
    }
}
