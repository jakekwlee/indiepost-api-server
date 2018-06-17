alter table `indiepost`.`Posts`
  drop column `likesCount`,
  drop column `commentsCount`;

alter table `indiepost`.`Users`
  drop column `password`;

update Users
set state = 'ACTIVATED'
where id > 0;

alter table `indiepost`.`Users`
  change column `email` `email` varchar(50) character set 'utf8' null default null;

alter table `indiepost`.`Users`
  drop index `UK_ncoa9bfasrql0x4nhmh1plxxy`;

alter table `indiepost`.`Users_Roles`
  drop foreign key `FKnftu1x2o0innaat555leqkoay`;

alter table `indiepost`.`Users_Roles`
  add constraint `FKnftu1x2o0innaat555leqkoay`
foreign key (`userId`)
references `indiepost`.`Users` (`id`)
  on delete cascade;

alter table `indiepost`.`Users`
  change column `username` `username` varchar(200) character set 'utf8' not null;

drop table `indiepost`.`Bookmarks`;