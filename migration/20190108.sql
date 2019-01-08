alter table `indiepost`.`CachedPostStats`
  drop
    column
    `legacyUniquePageviews`,
  drop
    column
    `legacyPageviews`;

alter table `indiepost`.`Posts`
  change column `content` `content`         longtext not null ,
  change column `displayName` `displayName` varchar(30) not null ,
  change column `excerpt` `excerpt`         varchar(300) not null ,
  change column `title` `title`             varchar(100) not null;

alter table `indiepost`.`Tags`
  change column `name` `name` varchar(50) not null;
