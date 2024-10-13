CREATE SCHEMA app
    AUTHORIZATION postgres;

CREATE TABLE app.email_status
(
id bigserial,
name character varying NOT NULL,
CONSTRAINT email_status_pk PRIMARY KEY (id)
);

ALTER TABLE IF EXISTS app.email_status
    OWNER to postgres;

CREATE TABLE app.emails
(
id uuid,
recipient_to character varying,
title character varying,
body_text character varying,
status_id bigint,
created_at TIMESTAMP NOT NULL,
updated_at TIMESTAMP NOT NULL,
CONSTRAINT emails_pk PRIMARY KEY (id),
CONSTRAINT emails_email_status_fk  FOREIGN KEY (status_id) REFERENCES app.email_status(id)
);

ALTER TABLE IF EXISTS app.emails
    OWNER to postgres;