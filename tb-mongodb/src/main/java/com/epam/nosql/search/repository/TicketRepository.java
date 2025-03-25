package com.epam.nosql.search.repository;

import com.epam.nosql.search.model.Category;
import com.epam.nosql.search.model.entity.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends MongoRepository<Ticket, String> {

    Page<Ticket> findByUserId(String userId, Pageable pageable);

    Page<Ticket> findByEventId(String eventId, Pageable pageable);

    boolean existsByEventIdAndPlaceAndCategory(String eventId, Integer place, Category category);
}
