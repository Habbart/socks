insert into role_table (role_name) values ('ROLE_WAREHOUSEMAN');
insert into role_table (role_name) values ('ROLE_CHIEF_OF_WAREHOUSE');

insert into user_table (name, surname, login, password, role_id)
values ( 'admin', 'admin', 'admin', '$2a$10$aV/4UlnWVY8gC/IitPXXze55Jc/KOzd9puz8Ev2FWoAHBE81.dfZm',
        (select  id from role_table where role_name = 'ROLE_CHIEF_OF_WAREHOUSE' group by role_name));