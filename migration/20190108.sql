alter table `indiepost`.`CachedPostStats`
  drop
    column
    `legacyUniquePageviews`,
  drop
    column
    `legacyPageviews`;

alter table `indiepost`.`Posts`
  change column `content` `content` longtext not null ,
  change column `displayName` `displayName` varchar(30) not null ,
  change column `excerpt` `excerpt` varchar(300) not null ,
  change column `title` `title` varchar(100) not null;

alter table `indiepost`.`Tags`
  change column `name` `name` varchar(50) not null;

alter table `indiepost`.`Posts`
  drop foreign key `FKmw94kk1l9cksjiklj42swgm2d`;
alter table `indiepost`.`Posts`
  change column `categoryId` `categoryId` bigint(20) null;
alter table `indiepost`.`Posts`
  add constraint `FKmw94kk1l9cksjiklj42swgm2d`
    foreign key (`categoryId`)
      references `indiepost`.`Categories`(`id`);
