USE `eventsdb`;

INSERT INTO `eventsdb`.`events`
(`name`, `description`, `start_date`, `end_date`)
VALUES
    ('event 1', 'This is event 1', now(), dateadd('DAY', 1, now())),
    ('event 2', 'This is event 2', now(), dateadd('DAY', 2, now())),
    ('event 3', 'This is event 3', now(), dateadd('DAY', 3, now())),
    ('event 4', 'This is event 4', now(), dateadd('DAY', 4, now())),
    ('event 5', 'This is event 5', now(), dateadd('DAY', 5, now())),
    ('event 6', 'This is event 6', now(), dateadd('DAY', 6, now())),
    ('event 7', 'This is event 7', now(), dateadd('DAY', 7, now()))
;
