use ticket_service;

truncate table user ;
truncate table role;

-- Test data for roles

insert into role(role_name) values ('USER');
insert into role(role_name) values ('ADMIN');


-- Test data for users.
insert into user(active, confirmation_token, email, first_name, last_name, matching_password, password, username, role_id) values (true , 'tokenexampleforuser1', 'petar@gmail.com', 'Pera', 'Peric', '$2a$10$Ww9QPzN8slLfRBMkGtF61egb6oFb3CpQjkFQ325DslgEtxvmqGZyK', '$2a$10$Ww9QPzN8slLfRBMkGtF61egb6oFb3CpQjkFQ325DslgEtxvmqGZyK', 'pera123', 1); -- password: test123
insert into user(active, confirmation_token, email, first_name, last_name, matching_password, password, username, role_id) values (true , 'tokenexampleforuser2', 'ben@gmail.com', 'Ben', 'El Fardu', '$2a$10$7ST7C.gyl55tOmZF8hGHiuyxYPveYTDJqtE4yxLkvsRzsmkFtRPqm', '$2a$10$7ST7C.gyl55tOmZF8hGHiuyxYPveYTDJqtE4yxLkvsRzsmkFtRPqm', 'bencz', 1); -- password: 1234

insert into user(active, confirmation_token, email, first_name, last_name, matching_password, password, username, role_id) values (true , 'tokenexampleforadmin', 'admin@gmail.com', 'Admin', 'AdminLastName', '$2a$10$MZ83gv7TtUsEI39EKOpGqu8LAMCcikJ5zRUzsenOS5ZT/Nfi7rPYK', '$2a$10$MZ83gv7TtUsEI39EKOpGqu8LAMCcikJ5zRUzsenOS5ZT/Nfi7rPYK', 'admin', 2); -- password : admin


-- Test data for address

insert into address(city, latitude, longitude, number, state, street) values ('Beograd', 22.3323, 0.3233, '1a', 'Srbija', 'Ljutice Bogdana');
insert into address(city, latitude, longitude, number, state, street) values ('Novi Sad', 21.3323, 12.3233, '2', 'Srbija', 'Sutjeska');
insert into address(city, latitude, longitude, number, state, street) values ('Kragujevac', 12.3113, 04.32233, '1', 'Srbija', 'Lepenicki bulevar');


-- Test data for locations
insert into location(location_name, address_id) values ('Stadion Rajko Mitic', 1);

insert into location(location_name, address_id) values ('Spens', 2);

insert into location(location_name, address_id) values ('Hala Jezero', 3);

insert into location(location_name, address_id) values ('Sajmiste', 3);


-- Test data for sectors
insert into sector(columns, rows, seats_number, sector_name, location_id) values (7, 10, 70, 'sectorSever', 1);
insert into sector(columns, rows, seats_number, sector_name, location_id) values (8, 8, 64, 'sectorJug', 1);
insert into sector(columns, rows, seats_number, sector_name, location_id) values (15, 12, 180, 'sectorIstok', 1);
insert into sector(columns, rows, seats_number, sector_name, location_id) values (24, 15, 360, 'sectorZapad', 1);

insert into sector(columns, rows, seats_number, sector_name, location_id) values (10, 10, 100, 'sectorSpens1', 2);
insert into sector(columns, rows, seats_number, sector_name, location_id) values (14, 20, 280, 'sectorSpens2', 2);
insert into sector(columns, rows, seats_number, sector_name, location_id) values (20, 25, 500, 'sectorSpens3', 2);

insert into sector(columns, rows, seats_number, sector_name, location_id) values (14, 30, 420, 'sectorParter', 3);

insert into sector(columns, rows, seats_number, sector_name, location_id) values (13, 20, 260, 'parterSajmiste', 4);
insert into sector(columns, rows, seats_number, sector_name, location_id) values (28, 20, 560, 'tribineSajmiste', 4);


-- Test data for manifestation
insert into manifestation(description, manifestation_category, name, start_time, location_id, end_time) values ('Neki opis manifestacije sa id 1, koja traje 3 dana.', 0, 'Koncert 1234', '2020-10-19 20:00:00', 1, '2020-10-21 20:00:00');
insert into manifestation(description, manifestation_category, name, start_time, location_id, end_time) values ('Neki opis manifestacije na spensu sa 3 sektora.', 2, 'KoncertNaSpensu', '2020-02-25 20:00:00', 2, '2020-02-26 20:00:00');
insert into manifestation(description, manifestation_category, name, start_time, location_id, end_time) values ('Neki opis manifestacije u Hali jezero.', 2, 'KoncertKg', '2020-03-01 20:00:00', 3, '2020-03-02 20:00:00');
insert into manifestation(description, manifestation_category, name, start_time, location_id, end_time) values ('Neki opis manifestacije u na sajmistu.', 0, 'ManSajmisteKg', '2020-08-20 20:00:00', 4, '2020-08-21 20:00:00');
insert into manifestation(description, manifestation_category, name, start_time, location_id, end_time) values ('Neki opis koncerta u na sajmistu koja pocinje uskoro.', 0, 'KoncertSajmisteKg', '2020-06-20 20:00:00', 4, '2020-06-21 20:00:00');

-- Test data for manifestation-days
insert into manifestation_days(description, name, start_time, manifestation_id) values ('Opis dana prvog za manifestaciju 1.', 'Dan1 man1', '2020-10-19 20:00:00', 1);
insert into manifestation_days(description, name, start_time, manifestation_id) values ('Opis dana drugog za manifestaciju 1.', 'Dan2 man1', '2020-10-20 20:00:00', 1);
insert into manifestation_days(description, name, start_time, manifestation_id) values ('Opis dana terceg za manifestaciju 1.', 'Dan3 man1', '2020-10-21 20:00:00', 1);

insert into manifestation_days(description, name, start_time, manifestation_id) values ('Opis dana prvig za manifestaciju 2.', 'Dan1 man2', '2020-02-25 20:00:00', 2);

insert into manifestation_days(description, name, start_time, manifestation_id) values ('Opis dana prvog za manifestaciju u Kg.', 'Dan1 man3', '2020-03-01 20:00:00', 3);

insert into manifestation_days(description, name, start_time, manifestation_id) values ('Opis dana prvog za manifestaciju u Sajmistu.', 'Dan1 man4', '2020-08-20 20:00:00', 4);

insert into manifestation_days(description, name, start_time, manifestation_id) values ('Opis dana prvog za koncert u Sajmistu, pocinje uskoro.', 'Koncert dan 1.', '2020-06-20 20:00:00', 5);


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

insert into manifestation_sector(price, manifestation_days_id, sector_id) values (800.00, 5, 8);

insert into manifestation_sector(price, manifestation_days_id, sector_id) values (350.00, 6, 9);
insert into manifestation_sector(price, manifestation_days_id, sector_id) values (400.00, 6, 10);

insert into manifestation_sector(price, manifestation_days_id, sector_id) values (800.00, 7, 9);
insert into manifestation_sector(price, manifestation_days_id, sector_id) values (1000.00, 7, 10);




-- Test data for ticket, will be added when user buy a ticket.
insert into ticket(purchase_confirmed, purchase_time, row_num, seat_num, manifestation_days_id, manifestation_sector_id, user_id) values (true , '2020-02-10 10:00:00', 1, 1, 1, 1, 1);
insert into ticket(purchase_confirmed, purchase_time, row_num, seat_num, manifestation_days_id, manifestation_sector_id, user_id) values (true , '2020-02-03 10:00:00', 6, 6, 2, 2, 2);
insert into ticket(purchase_confirmed, purchase_time, row_num, seat_num, manifestation_days_id, manifestation_sector_id, user_id) values (true , '2020-02-07 09:00:00', 4, 2, 1, 1, 1);
insert into ticket(purchase_confirmed, purchase_time, row_num, seat_num, manifestation_days_id, manifestation_sector_id, user_id) values (true , '2020-02-01 11:00:00', 5, 2, 2, 2, 2);
insert into ticket(purchase_confirmed, purchase_time, row_num, seat_num, manifestation_days_id, manifestation_sector_id, user_id) values (true , '2020-02-01 11:00:00', 1, 1, 5, 16, 2);

-- Tickets for reservations.
insert into ticket(purchase_confirmed, purchase_time, row_num, seat_num, manifestation_days_id, manifestation_sector_id, user_id) values (false , '2020-02-11 10:00:00', 2, 2, 1, 1, 1);
insert into ticket(purchase_confirmed, purchase_time, row_num, seat_num, manifestation_days_id, manifestation_sector_id, user_id) values (false , '2020-02-10 10:00:00', 3, 3, 1, 1, 1);
insert into ticket(purchase_confirmed, purchase_time, row_num, seat_num, manifestation_days_id, manifestation_sector_id, user_id) values (false , '2020-02-11 13:00:00', 1, 3, 2, 2, 2);
insert into ticket(purchase_confirmed, purchase_time, row_num, seat_num, manifestation_days_id, manifestation_sector_id, user_id) values (false , '2020-02-10 14:00:00', 3, 4, 2, 2, 2);
insert into ticket(purchase_confirmed, purchase_time, row_num, seat_num, manifestation_days_id, manifestation_sector_id, user_id) values (false , '2020-02-20 14:00:00', 10, 2, 2, 2, 2);

-- Test data for reservations, will be added when user reserve a ticket.
insert into reservation(active, exp_days, ticket_id, user_id) values (true, 10, 6, 1);
insert into reservation(active, exp_days, ticket_id, user_id) values (true, 10, 7, 1);

insert into reservation(active, exp_days, ticket_id, user_id) values (true, 10, 8, 2);
insert into reservation(active, exp_days, ticket_id, user_id) values (true, 10, 9, 2);
insert into reservation(active, exp_days, ticket_id, user_id) values (true, 10, 10, 2);