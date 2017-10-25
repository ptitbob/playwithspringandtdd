create sequence person_seq start with 1 increment by 1
create table address (address_id bigint not null, primary key (address_id))
create table person (person_id bigint not null, firstname varchar(100), lastname varchar(100), main_address_id bigint, primary key (person_id))
alter table person add constraint FKala6qxy4rdh7r0h01mq0fiyhd foreign key (main_address_id) references address
