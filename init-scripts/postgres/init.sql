CREATE TABLE events
(
    id    BIGINT GENERATED ALWAYS AS IDENTITY,
    title VARCHAR(50)   NOT NULL,
    date  TIMESTAMP     NOT NULL,
    ticket_price DECIMAL(6, 2) NOT NULL DEFAULT (0)
);

CREATE TABLE users
(
    id    BIGINT GENERATED ALWAYS AS IDENTITY,
    name  VARCHAR(50) NOT NULL,
    email VARCHAR(50) NOT NULL
);

CREATE TABLE tickets
(
    id       BIGINT GENERATED ALWAYS AS IDENTITY,
    user_id  BIGINT,
    event_id BIGINT,
    place    INT         NOT NULL,
    category VARCHAR(50) NOT NULL
);

CREATE TABLE user_accounts
(
    id      BIGINT GENERATED ALWAYS AS IDENTITY,
    user_id BIGINT,
    money   DECIMAL(6, 2) NOT NULL
);

ALTER TABLE events
    ADD CONSTRAINT pk_events PRIMARY KEY (id);
ALTER TABLE events
    ADD CONSTRAINT uq_events_title_date UNIQUE (title, date);

ALTER TABLE users
    ADD CONSTRAINT pk_users PRIMARY KEY (id);
ALTER TABLE users
    ADD CONSTRAINT uq_users_email UNIQUE (email);

ALTER TABLE tickets
    ADD CONSTRAINT pk_tickets PRIMARY KEY (id);
ALTER TABLE tickets
    ADD CONSTRAINT fk_tickets_users FOREIGN KEY (user_id) REFERENCES users (id);
ALTER TABLE tickets
    ADD CONSTRAINT fk_tickets_events FOREIGN KEY (event_id) REFERENCES events (id);
ALTER TABLE tickets
    ADD CONSTRAINT uq_tickets_event_id_place UNIQUE (event_id, place);
ALTER TABLE tickets
    ADD CONSTRAINT uq_tickets_user_id_event_id_place UNIQUE (user_id, event_id, place);

ALTER TABLE user_accounts
    ADD CONSTRAINT pk_user_accounts PRIMARY KEY (id);
ALTER TABLE user_accounts
    ADD CONSTRAINT fk_user_accounts_users FOREIGN KEY (user_id) REFERENCES users (id);

insert into users values (default, 'Alan', 'alan@gmail.com'),
                         (default, 'Kate', 'kate@gmail.com'),
                         (default, 'Max', 'max@gmail.com'),
                         (default, 'Sara', 'sara@gmail.com'),
                         (default, 'Alex', 'alex@gmail.com'),
                         (default, 'Alex', 'anotheralex@gmail.com');

insert into events values (default, 'First event', '2022-05-18 15:30', 100),
                          (default, 'Second event', '2022-05-15, 21:00', 300),
                          (default, 'Third event', '2022-05-16 12:00', 500),
                          (default, 'Fourth event', '2022-05-15 21:00', 450),
                          (default, 'Third event', '2022-05-25 9:10', 1000),
                          (default, 'Fifth event', '2022-06-1 14:20', 230);

insert into tickets values (default, 1, 1, 10, 'BAR'),
                           (default, 4, 3, 2, 'PREMIUM'),
                           (default, 2, 2, 4, 'STANDARD'),
                           (default, 1, 4, 20, 'BAR'),
                           (default, 5, 1, 11, 'PREMIUM'),
                           (default, 3, 5, 1, 'STANDARD');

insert into user_accounts values (default, 1, 1000),
                                 (default, 2, 1500);
