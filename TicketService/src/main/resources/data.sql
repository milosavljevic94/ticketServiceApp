use ticket_service;

truncate table user ;
truncate table role;

#Test data for roles
insert into role(role_name)
values ('USER');

insert into role(role_name)
values ('ADMIN');

#test data for users
insert into user(email, username, active, first_name, last_name, password, role_id, confirmation_token, matching_password)
values ('pera@gmail.com' ,'peraBot', true, 'Pera', 'Peric', '$2y$12$2GosEjiRgP3WOPDvZy6YR.z2FI4y1k1EnmvXflNbRv/LFdnAE/zPu', 1, 'proba.proba', '$2y$12$2GosEjiRgP3WOPDvZy6YR.z2FI4y1k1EnmvXflNbRv/LFdnAE/zPu');

insert into user(email, username, active, first_name, last_name, password, role_id, confirmation_token, matching_password)
values ('stefa@gmail.com', 'stefaBot', true, 'Stefan', 'Stefic', '$2y$12$rV9p3tZKtv3rYC5klTdp7Oyj0DwwEET5FRfxVoVHVIqIeGttSHdDy', 1, 'dasdas.fdfd.fdfd', '$2y$12$rV9p3tZKtv3rYC5klTdp7Oyj0DwwEET5FRfxVoVHVIqIeGttSHdDy'); #1234

insert into user(email, username, active, first_name, last_name, password,role_id, confirmation_token, matching_password)
values ('ana@gmail.com', 'anaBoot', true, 'Ana', 'Anic', '$2y$12$WTnOKP18KP5.DlI90hoThuFhFPkqwfzNVGjWZk3rcPRjALx.gPhza', 2, 'dfadasd.da.da', '$2y$12$WTnOKP18KP5.DlI90hoThuFhFPkqwfzNVGjWZk3rcPRjALx.gPhza');

#Test data for ticket
insert into ticket(row_num, seat_num)
values(12,4);

insert into ticket(row_num, seat_num)
values(11,22);

insert into ticket(row_num, seat_num)
values(13,33);

#Test data for reservations
insert into reservation(active, exp_days, ticket_id, user_id)
values (true, 3, 1, 3);

insert into reservation(active, exp_days, ticket_id, user_id)
values (true, 5, 2, 2);

insert into reservation(active, exp_days, ticket_id, user_id)
values (true, 33, 3, 3);

#Test data for address
insert into address(city, latitude, longitude, number, state, street)
values ('Beograd', 22.3323, 0.3233, '1a', 'Srbija', 'Ljutice Bogdana');

#Test data for locations
insert into location(location_name, address_id)
values ('Stadion Rajko Mitic', 1);

#Test data for sectors
insert into sector(columns, rows, seats_number, sector_name, location_id)
values (12, 22, 260, 'sectorSever', 1);
insert into sector(columns, rows, seats_number, sector_name, location_id)
values (10, 30, 300, 'sectorJug', 1);
insert into sector(columns, rows, seats_number, sector_name, location_id)
values (40, 30, 1200, 'sectorIstok', 1);
insert into sector(columns, rows, seats_number, sector_name, location_id)
values (50, 40, 2000, 'sectorZapad', 1);

#Test data for manifestation
insert into manifestation(description, manifestation_category, name, start_time, location_id, end_time)
values ('Neki opis manifestacije.', 2, 'Koncert1234', '2020-02-19 20:00:00', 1, '2020-02-21 20:00:00');