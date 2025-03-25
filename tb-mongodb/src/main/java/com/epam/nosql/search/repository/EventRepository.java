package com.epam.nosql.search.repository;

import com.epam.nosql.search.model.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface EventRepository extends MongoRepository<Event, String> {

    Page<Event> findByTitle(String title, Pageable pageable);

    Page<Event> findByDate(LocalDate day, Pageable pageable);

    Boolean existsByTitleAndDate(String title, LocalDate date);
}
