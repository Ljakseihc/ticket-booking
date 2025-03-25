package com.epam.nosql.migration.job.repository.mongodb;

import com.epam.nosql.migration.job.model.mogodb.UserDoc;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDocRepository extends MongoRepository<UserDoc, String> {
}
