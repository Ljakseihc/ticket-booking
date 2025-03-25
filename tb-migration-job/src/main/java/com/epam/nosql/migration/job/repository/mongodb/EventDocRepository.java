package com.epam.nosql.migration.job.repository.mongodb;

import com.epam.nosql.migration.job.model.mogodb.EventDoc;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventDocRepository extends MongoRepository<EventDoc, String> {
}
