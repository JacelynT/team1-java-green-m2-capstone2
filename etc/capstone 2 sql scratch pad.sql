SELECT category.name
FROM category
JOIN category_venue ON category.id = category_venue.category_id
WHERE venue_id = 3;

-------------------------------------------

START TRANSACTION;

INSERT INTO state (abbreviation, name) VALUES ('DU', 'DummyState');
INSERT INTO city (name, state_abbreviation) VALUES ('DummyCity', 'DU');
INSERT INTO venue (name, city_id, description) VALUES ('Dummy Venue',(SELECT id FROM city WHERE name = 'DummyCity'),'Yep, still 100% a test.');

INSERT INTO space (venue_id, name, is_accessible, open_from, open_to, daily_rate, max_occupancy)
VALUES ((SELECT id FROM venue WHERE name = 'Dummy Venue'),'Test Space1',true,3,7,8.25,200);

INSERT INTO space (venue_id, name, is_accessible, open_from, open_to, daily_rate, max_occupancy)
VALUES ((SELECT id FROM venue WHERE name = 'Dummy Venue'),'Test Space4',false,4,11,8.25,200);

INSERT INTO reservation (space_id, number_of_attendees, start_date, end_date, reserved_for) 
VALUES ((SELECT id FROM space WHERE name = 'Test Space4'),50,'2020-06-12','2020-06-17','Dummy Guest');

SELECT DISTCINT  
FROM space 
LEFT JOIN reservation ON reservation.space_id = space.id
WHERE space.venue_id = 2
AND ((space.open_from <= 1) OR (space.open_from IS NULL))
AND ((space.open_to >= 1) OR (space.open_to IS NULL))
AND space.max_occupancy >= 5
AND ((('2020-01-01'::DATE < reservation.start_date OR reservation.start_date IS NULL) AND ('2020-01-06'::DATE < reservation.start_date OR reservation.start_date IS NULL))
OR (('2020-01-01'::DATE > reservation.end_date OR reservation.end_date IS NULL) AND ('2020-01-06'::DATE > reservation.end_date OR reservation.end_date IS NULL)))
;

--AND '2020-06-15' NOT BETWEEN reservation.start_date AND reservation.end_date
--AND '2020-06-20' NOT BETWEEN reservation.start_date AND reservation.end_date;

ROLLBACK;

-----------------

SELECT daily_rate::decimal(9,2)
FROM space;

SELECT *
FROM space
WHERE venue_id = 2

