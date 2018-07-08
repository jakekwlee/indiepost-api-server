update Users
set lastLogin = now(), updatedAt = now()
where id > 0;