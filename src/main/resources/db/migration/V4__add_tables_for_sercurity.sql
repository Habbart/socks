
drop table if exists user_table;
drop table if exists role_table cascade;


create table role_table (
    id bigserial primary key ,
    role_name varchar(55)
);

create table user_table(
    id bigserial primary key ,
    name varchar(35),
    surname varchar(55),
    login varchar(35),
    password varchar(500),
    role_id int,
    foreign key (role_id) references role_table(id)
);


