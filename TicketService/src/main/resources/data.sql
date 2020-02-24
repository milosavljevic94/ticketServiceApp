use ticket_service;

truncate table user ;
truncate table role;

#Test data for roles
insert into role(role_name)
values ('USER');

insert into role(role_name)
values ('ADMIN');


#test data for users, will be added when register.


#Test data for ticket, will be added when user buy a ticket.


#Test data for reservations, will be added when user reserve a ticket.


#Test data for address
insert into address(city, latitude, longitude, number, state, street)
values ('Beograd', 22.3323, 0.3233, '1a', 'Srbija', 'Ljutice Bogdana');

insert into address(city, latitude, longitude, number, state, street)
values ('Novi Sad', 21.3323, 12.3233, '2', 'Srbija', 'Sutjeska');

#Test data for locations
insert into location(location_name, address_id)
values ('Stadion Rajko Mitic', 1);

insert into location(location_name, address_id)
values ('Spens', 2);

#Test data for sectors
insert into sector(columns, rows, seats_number, sector_name, location_id)
values (12, 22, 260, 'sectorSever', 1);
insert into sector(columns, rows, seats_number, sector_name, location_id)
values (10, 30, 300, 'sectorJug', 1);
insert into sector(columns, rows, seats_number, sector_name, location_id)
values (40, 30, 1200, 'sectorIstok', 1);
insert into sector(columns, rows, seats_number, sector_name, location_id)
values (50, 40, 2000, 'sectorZapad', 1);

insert into sector(columns, rows, seats_number, sector_name, location_id)
values (10, 40, 400, 'sectorSpens1', 2);
insert into sector(columns, rows, seats_number, sector_name, location_id)
values (10, 30, 300, 'sectorSpens2', 2);
insert into sector(columns, rows, seats_number, sector_name, location_id)
values (20, 40, 800, 'sectorSpens3', 2);

#Test data for manifestation
insert into manifestation(description, manifestation_category, name, start_time, location_id, end_time)
values ('Neki opis manifestacije sa id 1, koja traje 3 dana.', 0, 'Koncert 1234', '2020-02-19 20:00:00', 1, '2020-02-21 20:00:00');

insert into manifestation(description, manifestation_category, name, start_time, location_id, end_time)
values ('Neki opis manifestacije na spensu sa 3 sektora.', 2, 'KoncertNaSpensu', '2020-02-25 20:00:00', 2, '2020-02-26 20:00:00');

#Test data for manifestation-days
insert into manifestation_days(description, name, start_time, manifestation_id)
values ('Opis dana prvog za manifestaciju 1.', 'Dan1 man1', '2020-02-19 20:00:00', 1);
insert into manifestation_days(description, name, start_time, manifestation_id)
values ('Opis dana drugog za manifestaciju 1.', 'Dan2 man1', '2020-02-20 20:00:00', 1);
insert into manifestation_days(description, name, start_time, manifestation_id)
values ('Opis dana terceg za manifestaciju 1.', 'Dan3 man1', '2020-02-21 20:00:00', 1);

insert into manifestation_days(description, name, start_time, manifestation_id)
values ('Opis dana prvig za manifestaciju 2.', 'Dan1 man2', '2020-02-25 20:00:00', 2);

#Test data for manifestation-sector
insert into manifestation_sector(price, manifestation_days_id, sector_id)
values (250.00, 1, 1);
insert into manifestation_sector(price, manifestation_days_id, sector_id)
values (350.00, 1, 2);
insert into manifestation_sector(price, manifestation_days_id, sector_id)
values (550.00, 1, 3);
insert into manifestation_sector(price, manifestation_days_id, sector_id)
values (750.00, 1, 4);

insert into manifestation_sector(price, manifestation_days_id, sector_id)
values (250.00, 2, 1);
insert into manifestation_sector(price, manifestation_days_id, sector_id)
values (350.00, 2, 2);
insert into manifestation_sector(price, manifestation_days_id, sector_id)
values (550.00, 2, 3);
insert into manifestation_sector(price, manifestation_days_id, sector_id)
values (750.00, 2, 4);

insert into manifestation_sector(price, manifestation_days_id, sector_id)
values (150.00, 3, 1);
insert into manifestation_sector(price, manifestation_days_id, sector_id)
values (250.00, 3, 2);
insert into manifestation_sector(price, manifestation_days_id, sector_id)
values (350.00, 3, 3);
insert into manifestation_sector(price, manifestation_days_id, sector_id)
values (450.00, 3, 4);

insert into manifestation_sector(price, manifestation_days_id, sector_id)
values (1150.00, 4, 5);
insert into manifestation_sector(price, manifestation_days_id, sector_id)
values (1450.00, 4, 6);
insert into manifestation_sector(price, manifestation_days_id, sector_id)
values (1550.00, 4, 7);