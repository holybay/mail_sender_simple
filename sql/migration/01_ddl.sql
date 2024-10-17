ALTER TABLE app.emails
	ADD COLUMN status character varying;

UPDATE app.emails
SET status = es."name"
FROM app.email_status es
WHERE status_id = es.id;

ALTER TABLE app.emails
	ALTER COLUMN status SET NOT NULL;

ALTER TABLE app.emails
	DROP CONSTRAINT emails_email_status_fk;

ALTER TABLE app.emails
	DROP COLUMN status_id;

DROP TABLE app.email_status;