USE `eventsdb`;

INSERT INTO `eventsdb`.`events`
(`name`, `description`, `start_date`, `end_date`)
VALUES
('event 1', 'This is event 1', CURDATE(), CURDATE() + 1),
('event 2', 'This is event 2', CURDATE(), CURDATE() + 2),
('event 3', 'This is event 3', CURDATE(), CURDATE() + 3),
('event 4', 'This is event 4', CURDATE(), CURDATE() + 4),
('event 5', 'This is event 5', CURDATE(), CURDATE() + 5),
('event 6', 'This is event 6', CURDATE(), CURDATE() + 6),
('event 7', 'This is event 7', CURDATE(), CURDATE() + 7);

