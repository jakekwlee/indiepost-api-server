alter table `indiepost`.`Posts`
  drop column `likesCount`,
  drop column `commentsCount`;
alter table `indiepost`.`Users`
  drop column `password`;
update Users
set state = 'ACTIVATED'
where id > 0;