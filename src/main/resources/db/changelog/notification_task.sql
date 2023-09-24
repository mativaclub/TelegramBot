--liquibase formatted sql

--changeset maria:4

CREATE TABLE notification_task (
    id SERIAL PRIMARY KEY,
    id_chat INT NOT NULL,
    notify_text VARCHAR(4096) NOT NULL,
    date_time TIMESTAMP NOT NULL
);

--changeset maria:5
ALTER TABLE notification_task ALTER COLUMN id TYPE BIGINT;
ALTER TABLE notification_task ALTER COLUMN id_chat TYPE BIGINT;

--changeset maria:6

DELETE FROM notification_task;