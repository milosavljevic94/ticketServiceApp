truncate table user ;
truncate table role;
truncate table user_roles;

insert into user(active, first_name, last_name, password)
values (true, 'Pera', 'Peric', '$2y$12$2GosEjiRgP3WOPDvZy6YR.z2FI4y1k1EnmvXflNbRv/LFdnAE/zPu');

insert into user(active, first_name, last_name, password)
values (true, 'Ana', 'Anic', '$2y$12$WTnOKP18KP5.DlI90hoThuFhFPkqwfzNVGjWZk3rcPRjALx.gPhza');

insert into role(role_name)
values ('USER');

insert into role(role_name)
values ('ADMIN');

insert into user_roles(user_id, role_id)
values (1,1);

insert into user_roles(user_id, role_id)
values (2,2);