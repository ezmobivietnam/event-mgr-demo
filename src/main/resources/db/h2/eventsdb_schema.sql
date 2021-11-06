-- -----------------------------------------------------
-- Schema eventsdb
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `eventsdb`;
USE `eventsdb` ;

-- -----------------------------------------------------
-- Table `eventsdb`.`events`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `events` ;

CREATE TABLE IF NOT EXISTS `events` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(32) NOT NULL,
  `description` VARCHAR(255) NULL,
  `start_date` DATETIME NOT NULL,
  `end_date` DATETIME NOT NULL,
  `last_update_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`))
;

-- CREATE UNIQUE INDEX `name_UNIQUE` ON `eventsdb`.`events` (`name` ASC) VISIBLE;
-- CREATE UNIQUE INDEX `EVT_UNIQUE` ON `eventsdb`.`events` (`name` ASC, `start_date` ASC, `end_date` ASC) VISIBLE;
CREATE UNIQUE INDEX `EVT_UNIQUE` ON `eventsdb`.`events` (`name` ASC, `start_date` ASC, `end_date` ASC);