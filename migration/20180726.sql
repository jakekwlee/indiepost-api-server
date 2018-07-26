alter table `indiepost`.`Roles`
  change column `name` `roleType` varchar(20) character set 'utf8' not null;

delete
from `indiepost`.`Users`
where `id` = '8';

delete
from `indiepost`.`Users`
where `id` = '9';

delete
from Users_Roles
where userId in(4, 5, 6, 7, 11, 14) and roleId > 1;

delete
from Users_Roles
where userId > 4 and roleId > 3;

insert into Users_Roles(userId, roleId)
values (2, 5);

update `indiepost` . `Users`
set `joinedAt` = '2016-12-04 10:10:59'
where `id` = '2';
