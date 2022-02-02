ALTER TABLE user_table add column is_blocked boolean;

UPDATE user_table set is_blocked = false
where id <> 0;


