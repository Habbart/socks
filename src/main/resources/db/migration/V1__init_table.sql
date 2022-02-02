DROP TABLE IF EXISTS socks;
CREATE TABLE socks(
                                    id bigserial primary key,
                                    color varchar(50),
                                    cotton_part int,
                                    quantity int
)