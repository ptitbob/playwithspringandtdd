-- TRUNCATE TABLE PERSON;

insert INTO Address(address_id) values(1);
insert into web_user(person_id, firstname, lastname, main_address_id) values (100, 'Anthony', 'Stark', 1);
insert into web_user(person_id, firstname, lastname, main_address_id) values (101, 'toto', 'titi', 1);

