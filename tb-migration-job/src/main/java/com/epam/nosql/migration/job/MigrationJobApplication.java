package com.epam.nosql.migration.job;

import com.epam.nosql.migration.job.service.MigrationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class MigrationJobApplication implements CommandLineRunner {

    private final MigrationService service;

    @Autowired
    public MigrationJobApplication(MigrationService service) {
        this.service = service;
    }

    public static void main(String[] args) {
        SpringApplication.run(MigrationJobApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("TASK START");
        service.migrateFromSQLToMongo();
        log.info("TASK DONE");
    }
}
