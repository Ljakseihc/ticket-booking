package com.epam.nosql.migration.job.repository.postgres;

import com.epam.nosql.migration.job.model.postgresql.EventTable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventTableRepository extends CrudRepository<EventTable, Long> {
}
