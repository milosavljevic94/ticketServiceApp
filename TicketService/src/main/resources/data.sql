truncate table user ;
truncate table role;

#Test data for roles
insert into role(role_name)
values ('USER');

insert into role(role_name)
values ('ADMIN');

#test data for users
insert into user(active, first_name, last_name, password, role_id)
values (true, 'Pera', 'Peric', '$2y$12$2GosEjiRgP3WOPDvZy6YR.z2FI4y1k1EnmvXflNbRv/LFdnAE/zPu', 1);

insert into user(active, first_name, last_name, password, role_id)
values (true, 'Stefan', 'Stefic', '$2y$12$rV9p3tZKtv3rYC5klTdp7Oyj0DwwEET5FRfxVoVHVIqIeGttSHdDy', 1); #1234

insert into user(active, first_name, last_name, password,role_id)
values (true, 'Ana', 'Anic', '$2y$12$WTnOKP18KP5.DlI90hoThuFhFPkqwfzNVGjWZk3rcPRjALx.gPhza', 2);

#Test data for reservations
insert into reservation(active, exp_days, ticket_id, user_id)
values (true, 3, 1, 3);

#Test data for ticket
insert into ticket(row_num, seat_num)
values(12,4);