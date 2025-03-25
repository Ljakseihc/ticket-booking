package com.epam.nosql.migration.job.repository.postgres;

import com.epam.nosql.migration.job.model.postgresql.TicketTable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketTableRepository extends CrudRepository<TicketTable, Long> {
}
