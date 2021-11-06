USE `eventsdb`;

INSERT INTO `eventsdb`.`events`
(`name`, `description`, `start_date`, `end_date`)
VALUES
    ('event 1', 'This is event 1', now(), date_add(now(), INTERVAL 1 DAY)),
    ('event 2', 'This is event 2', now(), date_add(now(), INTERVAL 2 DAY)),
    ('event 3', 'This is event 3', now(), date_add(now(), INTERVAL 3 DAY)),
    ('event 4', 'This is event 4', now(), date_add(now(), INTERVAL 4 DAY)),
    ('event 5', 'This is event 5', now(), date_add(now(), INTERVAL 5 DAY)),
    ('event 6', 'This is event 6', now(), date_add(now(), INTERVAL 6 DAY)),
    ('event 7', 'This is event 7', now(), date_add(now(), INTERVAL 7 DAY))
;

