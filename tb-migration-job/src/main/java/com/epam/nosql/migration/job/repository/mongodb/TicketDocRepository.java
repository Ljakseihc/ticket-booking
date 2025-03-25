package com.epam.nosql.migration.job.repository.mongodb;

import com.epam.nosql.migration.job.model.mogodb.TicketDoc;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketDocRepository extends MongoRepository<TicketDoc, String> {
}
