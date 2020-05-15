use kts_test;


-- Test data for roles
insert into role (role_name) values ('USER');
insert into role(role_name) values ('ADMIN');

-- test data for users.
insert into user(active, confirmation_token, email, first_name, last_name, matching_password, password, username, role_id) values (true , 'tokenexamplefortest', 'test1@gmail.com', 'TestName', 'TestLastName', '$2a$10$zVmg.K.gRtclVbX3DKnbR.ItYbKM6DXrbphFjmNDtPQc/ugKemuoW', '$2a$10$zVmg.K.gRtclVbX3DKnbR.ItYbKM6DXrbphFjmNDtPQc/ugKemuoW', 'test1', 1); -- password: test123
insert into user(active, confirmation_token, email, first_name, last_name, matching_password, password, username, role_id) values (true , 'tokenexampleforadmin', 'test_admin@gmail.com', 'TestAdmin', 'TestAdminLastName', '$2a$10$0XFCOGokcC4jkYjlS96eqeGL9P6miUr.ogavuGiRYN0SYlKd0I6Aa', '$2a$10$0XFCOGokcC4jkYjlS96eqeGL9P6miUr.ogavuGiRYN0SYlKd0I6Aa', 'testAdmin', 2); -- password: admin

-- Test data for address
insert into address(city, latitude, longitude, number, state, street) values ('Beograd', 22.3323, 0.3233, '1a', 'Srbija', 'Ljutice Bogdana');

insert into address(city, latitude, longitude, number, state, street) values ('Novi Sad', 21.3323, 12.3233, '2', 'Srbija', 'Sutjeska');

-- Test data for locations
insert into location(location_name, address_id) values ('Stadion Rajko Mitic', 1);

insert into location(location_name, address_id) values ('Spens', 2);

-- Test data for sectors
insert into sector(columns, rows, seats_number, sector_name, location_id) values (12, 22, 264, 'sectorSever', 1);
insert into sector(columns, rows, seats_number, sector_name, location_id) values (10, 30, 300, 'sectorJug', 1);
insert into sector(columns, rows, seats_number, sector_name, location_id) values (40, 30, 1200, 'sectorIstok', 1);
insert into sector(columns, rows, seats_number, sector_name, location_id) values (50, 40, 2000, 'sectorZapad', 1);

insert into sector(columns, rows, seats_number, sector_name, location_id) values (10, 40, 400, 'sectorSpens1', 2);
insert into sector(columns, rows, seats_number, sector_name, location_id) values (10, 30, 300, 'sectorSpens2', 2);
insert into sector(columns, rows, seats_number, sector_name, location_id) values (20, 40, 800, 'sectorSpens3', 2);

-- Test data for manifestation
insert into manifestation(description, manifestation_category, name, start_time, location_id, end_time) values ('Neki opis manifestacije sa id 1, koja traje 3 dana.', 0, 'Koncert 1234', '2020-10-19 20:00:00', 1, '2020-10-21 20:00:00');

insert into manifestation(description, manifestation_category, name, start_time, location_id, end_time) values ('Neki opis manifestacije na spensu sa 3 sektora.', 2, 'KoncertNaSpensu', '2020-02-25 20:00:00', 2, '2020-02-26 20:00:00');

-- Test data for manifestation-days
insert into manifestation_days(description, name, start_time, manifestation_id) values ('Opis dana prvog za manifestaciju 1.', 'Dan1 man1', '2020-10-19 20:00:00', 1);
insert into manifestation_days(description, name, start_time, manifestation_id) values ('Opis dana drugog za manifestaciju 1.', 'Dan2 man1', '2020-10-20 20:00:00', 1);
insert into manifestation_days(description, name, start_time, manifestation_id) values ('Opis dana terceg za manifestaciju 1.', 'Dan3 man1', '2020-10-21 20:00:00', 1);

insert into manifestation_days(description, name, start_time, manifestation_id) values ('Opis dana 1 za manifestaciju 2.', 'Dan1 man2', '2020-02-25 20:00:00', 2);
-- Day with valid date
insert into manifestation_days(description, name, start_time, manifestation_id) values ('Opis dana 2 za manifestaciju 2.', 'Dan2 man2', '2020-05-20 20:00:00', 2);


-- Test data for manifestation-sector
insert into manifestation_sector(price, manifestation_days_id, sector_id) values (250.00, 1, 1);
insert into manifestation_sector(price, manifestation_days_id, sector_id) values (350.00, 1, 2);
insert into manifestation_sector(price, manifestation_days_id, sector_id) values (550.00, 1, 3);
insert into manifestation_sector(price, manifestation_days_id, sector_id) values (750.00, 1, 4);

insert into manifestation_sector(price, manifestation_days_id, sector_id) values (250.00, 2, 1);
insert into manifestation_sector(price, manifestation_days_id, sector_id) values (350.00, 2, 2);
insert into manifestation_sector(price, manifestation_days_id, sector_id) values (550.00, 2, 3);
insert into manifestation_sector(price, manifestation_days_id, sector_id) values (750.00, 2, 4);

insert into manifestation_sector(price, manifestation_days_id, sector_id) values (150.00, 3, 1);
insert into manifestation_sector(price, manifestation_days_id, sector_id) values (250.00, 3, 2);
insert into manifestation_sector(price, manifestation_days_id, sector_id) values (350.00, 3, 3);
insert into manifestation_sector(price, manifestation_days_id, sector_id) values (450.00, 3, 4);

insert into manifestation_sector(price, manifestation_days_id, sector_id) values (1150.00, 4, 5);
insert into manifestation_sector(price, manifestation_days_id, sector_id) values (1450.00, 4, 6);
insert into manifestation_sector(price, manifestation_days_id, sector_id) values (1550.00, 4, 7);

-- Test data for ticket.
insert into ticket(purchase_confirmed, purchase_time, row_num, seat_num, manifestation_days_id, manifestation_sector_id, user_id) values (true , '2020-02-10 10:00:00', 1, 1, 1, 1, 1);
insert into ticket(purchase_confirmed, purchase_time, row_num, seat_num, manifestation_days_id, manifestation_sector_id, user_id) values (true , '2020-02-03 10:00:00', 6, 6, 2, 2, 1);

-- Tickets for reservations.
insert into ticket(purchase_confirmed, purchase_time, row_num, seat_num, manifestation_days_id, manifestation_sector_id, user_id) values (false , '2020-02-11 10:00:00', 2, 2, 1, 1, 1);
insert into ticket(purchase_confirmed, purchase_time, row_num, seat_num, manifestation_days_id, manifestation_sector_id, user_id) values (false , '2020-02-10 10:00:00', 3, 3, 1, 1, 1);
-- Ticket for admin.
insert into ticket(purchase_confirmed, purchase_time, row_num, seat_num, manifestation_days_id, manifestation_sector_id, user_id) values (false , '2020-02-09 10:00:00', 4, 4, 2, 2, 2);

-- Test data for reservations.
insert into reservation(active, exp_days, ticket_id, user_id) values (true, 10, 3, 1);
insert into reservation(active, exp_days, ticket_id, user_id) values (true, 10, 4, 1);

insert into reservation(active, exp_days, ticket_id, user_id) values (true, 10, 5, 2);
