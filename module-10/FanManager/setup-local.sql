-- Run separately for home practice only, before starting the application.
-- The instructor supplies this table; FanManager never creates or deletes it.
USE databasedb;
CREATE TABLE IF NOT EXISTS fans (
    ID INTEGER PRIMARY KEY,
    firstname VARCHAR(25),
    lastname VARCHAR(25),
    favoriteteam VARCHAR(25)
) ENGINE=InnoDB;
-- Existing rows with these IDs are preserved.
INSERT IGNORE INTO fans (ID, firstname, lastname, favoriteteam) VALUES
(1, 'Sam', 'Dirr', 'Cubs'),
(2, 'Alex', 'Morgan', 'Bulls'),
(3, 'Jordan', 'Taylor', 'Bears');
